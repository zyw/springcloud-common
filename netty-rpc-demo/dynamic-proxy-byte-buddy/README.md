# Byte Buddy学习笔记
转载地址：[Byte Buddy学习笔记]()

## 1. 简介

`Byte Buddy`是一个JVM的运行时代码生成器，你可以利用它创建任何类，且不像`JDK`动态代理那样强制实现一个接口。`Byte Buddy`还提供了简单的`API`，便于手工、通过`Java Agent`，或者在构建期间修改字节码。

`Java`反射`API`可以做很多和字节码生成器类似的工作，但是它具有以下缺点：

1. 相比硬编码的方法调用，使用 反射 `API` 非常慢
2. 反射 `API` 能绕过类型安全检查

比起JDK动态代理、`cglib`、`Javassist`，`Byte Buddy`在性能上具有优势。

## 2. 入门

### 2.1. 创建新类型

下面是一个最简单的例子：

```java
Class<?> dynamicType = new ByteBuddy()
  // 指定父类
  .subclass(Object.class)
   // 根据名称来匹配需要拦截的方法
  .method(ElementMatchers.named("toString"))
  // 拦截方法调用，返回固定值
  .intercept(FixedValue.value("Hello World!"))
  // 产生字节码
  .make()
  // 加载类
  .load(getClass().getClassLoader())
  // 获得Class对象
  .getLoaded();
 
assertThat(dynamicType.newInstance().toString(), is("Hello World!"));
```

`ByteBuddy`利用`Implementation`接口来表示一个动态定义的方法，`FixedValue.value`就是该接口的实现。

```java
/**
 * This implementation returns a fixed value for a method. Other than the {@link net.bytebuddy.implementation.StubMethod}
 * implementation, this implementation allows to determine a specific value which must be assignable to the returning value
 * of any instrumented method. Otherwise, an exception will be thrown.
 *
 * @see FieldAccessor
 */
@HashCodeAndEqualsPlugin.Enhance
public abstract class FixedValue implements Implementation {}
```

完全实现`Implementation`比较繁琐，因此实际情况下会使用`MethodDelegation`代替。使用`MethodDelegation`，你可以在一个`POJO`中实现方法拦截器：

```java
public class GreetingInterceptor {
  // 方法签名随意
  public Object greet(Object argument) {
    return "Hello from " + argument;
  }
}
 
Class<? extends java.util.function.Function> dynamicType = new ByteBuddy()
  // 实现一个Function子类
  .subclass(java.util.function.Function.class)
  .method(ElementMatchers.named("apply"))
  // 拦截Function.apply调用，委托给GreetingInterceptor处理
  .intercept(MethodDelegation.to(new GreetingInterceptor()))
  .make()
  .load(getClass().getClassLoader())
  .getLoaded();
 
assertThat((String) dynamicType.newInstance().apply("Byte Buddy"), is("Hello from Byte Buddy"));
```

编写拦截器时，你可以指定一些注解，`ByteBuddy`会自动注入：

```java
public class GeneralInterceptor {
  // 提示ByteBuddy根据被拦截方法的实际类型，对此拦截器的返回值进行Cast
  @RuntimeType
  //                      所有入参的数组
  public Object intercept(@AllArguments Object[] allArguments,
  //                      被拦截的原始方法
                          @Origin Method method) {
  }
}
```

### 2.2. 修改已有类型

上面的两个例子中，我们利用`ByteBuddy`创建了指定接口的新子类型，`ByteBuddy`也可以用来修改已存在的。

`ByteBuddy`提供了便捷的创建`Java Agent`的`API`，本节的例子就是通过`Java Agent`方式来修改已存在的Java类型：

```java
public class TimerAgent {
  public static void premain(String arguments, 
                               Instrumentation instrumentation) {
      new AgentBuilder.Default()
        // 匹配被拦截方法
        .type(ElementMatchers.nameEndsWith("Timed"))
        .transform(
            (builder, type, classLoader, module) -> 
                builder.method(ElementMatchers.any()) .intercept(MethodDelegation.to(TimingInterceptor.class))
        ).installOn(instrumentation);
    }
  }
 
  public class TimingInterceptor {
    @RuntimeType
    public static Object intercept(@Origin Method method, 
                                   // 调用该注解后的Runnable/Callable，会导致调用被代理的非抽象父方法
                                   @SuperCall Callable<?> callable) {
      long start = System.currentTimeMillis();
      try {
        return callable.call();
      } finally {
        System.out.println(method + " took " + (System.currentTimeMillis() - start));
      }
    }
}
```



## 3. API

### 3.1. 创建类

#### 3.1.1. subclass

调用此方法可以创建一个目标类的子类：

```java
DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
  .subclass(Object.class)
  .name("example.Type")  // 子类的名称
  .make();
```

如果不指定子类名称，`Byte Buddy`会有一套自动的策略来生成。你还可以指定子类命名策略：

```java
DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
  .with(new NamingStrategy.AbstractBase() {
    @Override
    public String subclass(TypeDescription superClass) {
        return "i.love.ByteBuddy." + superClass.getSimpleName();
    }
  })
  .subclass(Object.class)
  .make();
```

#### 3.1.2. 加载类

上节创建的`DynamicType.Unloaded`，代表一个尚未加载的类，你可以通过`ClassLoadingStrategy`来加载这种类。 

如果不指定`ClassLoadingStrategy`，`Byte Buffer`根据你提供的`ClassLoader`来推导出一个策略，内置的策略定义在枚举`ClassLoadingStrategy.Default`中：

1. `WRAPPER`：创建一个新的`Wrapping`类加载器
2. `CHILD_FIRST`：类似上面，但是子加载器优先负责加载目标类
3. `INJECTION`：利用反射机制注入动态类型

示例：

```java
Class<?> type = new ByteBuddy()
  .subclass(Object.class)
  .make()
  .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
  .getLoaded();
```

### 3.2. 修改类

#### 3.2.1. redefine

重定义一个类时，`Byte Buddy` 可以对一个已有的类添加属性和方法，或者删除已经存在的方法实现。新添加的方法，如果签名和原有方法一致，则原有方法会消失。

#### 3.2.2. rebase

类似于`redefine`，但是原有的方法不会消失，而是被重命名，添加后缀 `$original`，例如类：

```java
class Foo {
  String bar() { return "bar"; }
}
```

在`rebase`之后，会变成：

```java
class Foo {
  String bar() { return "foo" + bar$original(); }
  private String bar$original() { return "bar"; }
}
```

#### 3.2.3. 重新加载类

得益于`JVM`的`HostSwap`特性，已加载的类可以被重新定义：

```java
// 安装Byte Buddy的Agent，除了通过-javaagent静态安装，还可以：
ByteBuddyAgent.install();
Foo foo = new Foo();
new ByteBuddy()
  .redefine(Bar.class)
  .name(Foo.class.getName())
  .make()
  .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
 
assertThat(foo.m(), is("bar"));
```

可以看到，即使时已经存在的对象，也会受到类`Reloading`的影响。

当前`HostSwap`具有限制：

1. 类再重新载入前后，必须具有相同的`Schema`，也就是方法、字段不能减少（可以增加）
2. 不支持具有静态初始化块的类

### 3.3. 操控未加载类

`Byte Buddy`提供了类似于`Javassist`的、操控未加载类的API。它在`TypePool`中维护类型的元数据`TypeDescription`：

```java
// 获取默认类型池
TypePool typePool = TypePool.Default.ofClassPath();
new ByteBuddy()
  .redefine(typePool.describe("foo.Bar").resolve(), // 根据名称进行解析类
            // ClassFileLocator用于定位到被修改类的.class文件
            ClassFileLocator.ForClassLoader.ofClassPath())
  .defineField("qux", String.class) // 定义一个新的字段
  .make()
  .load(ClassLoader.getSystemClassLoader());
assertThat(Bar.class.getDeclaredField("qux"), notNullValue());
```

### 3.4. 拦截方法

#### 3.4.1. 匹配方法

`Byte Buddy`提供了很多用于匹配方法的`DSL`：

```java
class Foo {
  public String bar() { return null; }
  public String foo() { return null; }
  public String foo(Object o) { return null; }
}
 
Foo dynamicFoo = new ByteBuddy()
  .subclass(Foo.class)
  // 匹配由Foo.class声明的方法
  .method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("One!"))
  // 匹配名为foo的方法
  .method(named("foo")).intercept(FixedValue.value("Two!"))
  // 匹配名为foo，入参数量为1的方法
  .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("Three!"))
  .make()
  .load(getClass().getClassLoader())
  .getLoaded()
  .newInstance();
```

#### 3.4.2. 委托方法

使用`MethodDelegation`可以将方法调用委托给任意`POJO`。`Byte Buddy`不要求`Source`（被委托类）、`Target`类的方法名一致：

```java
class Source {
  public String hello(String name) { return null; }
}
 
String helloWorld = new ByteBuddy()
  .subclass(Source.class)
  .method(named("hello")).intercept(MethodDelegation.to(Target.class))
  .make()
  .load(getClass().getClassLoader())
  .getLoaded()
  .newInstance()
  .hello("World");
```

`Target`的实现可以如下： 

```java
class Target {
  public static String hello(String name) {
    return "Hello " + name + "!";
  }
}
```

也可以如下：

```java
class Target {
  public static String intercept(String name) { return "Hello " + name + "!"; }
  public static String intercept(int i) { return Integer.toString(i); }
  public static String intercept(Object o) { return o.toString(); }
}
```

前一个实现很好理解，那么后一个呢，`Byte Buddy`到底会委托给哪个方法？`Byte Buddy`遵循一个最接近原则：

1. `intercept(int)`因为参数类型不匹配，直接`Pass`
2. 另外两个方法参数都匹配，但是`intercept(String)`类型更加接近，因此会委托给它

#### 3.4.3. 参数绑定

你可以在`Target`的方法中使用注解进行参数绑定：

```java
void foo(Object o1, Object o2)
// 等价于
void foo(@Argument(0) Object o1, @Argument(1) Object o2)
```

全部注解如下表：

```java
//蚂蚁金服byte buddy的方法拦截中的注解，拦截类BytebuddyInvocationHandler
@RuntimeType
public Object byteBuddyInvoke(@This Object proxy, @Origin Method method, @AllArguments @RuntimeType Object[] args){}
```

| 注解            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| `@Argument`     | 绑定单个参数                                                 |
| `@AllArguments` | 绑定所有参数的数组                                           |
| `@This`         | 当前被拦截的、动态生成的那个对象                             |
| `@Super`        | 当前被拦截的、动态生成的那个对象的父类对象                   |
| `@Origin`       | 可以绑定到以下类型的参数：<br/>    `Method` 被调用的原始方法<br/>    `Constructor` 被调用的原始构造器<br/>    `Class` 当前动态创建的类<br/>    `MethodHandle`<br/>    `MethodType`<br/>    `String`动态类的`toString()`的返回值<br/>    `int`动态方法的修饰符 |
| `@DefaultCall`  | 调用默认方法而非`super`的方法                                |
| `@SuperCall`    | 用于调用父类版本的方法                                       |
| `@Super`        | 注入父类型对象，可以是接口，从而调用它的任何方法             |
| `@RuntimeType`  | 可以用在返回值、参数上，提示`ByteBuddy`禁用严格的类型检查    |
| `@Empty`        | 注入参数的类型的默认值                                       |
| `@StubValue`    | 注入一个存根值。对于返回引用、`void`的方法，注入`null`；对于返回原始类型的方法，注入`0` |
| `@FieldValue`   | 注入被拦截对象的一个字段的值                                 |
| `@Morph`        | 类似于`@SuperCall`，但是允许指定调用参数                     |

### 3.5. 添加字段

```java
Class<? extends UserType> dynamicUserType = new ByteBuddy()
  .subclass(UserType.class)
  .defineField("interceptor", Interceptor.class, Visibility.PRIVATE);
```

方法调用也可以委托给字段（而非外部对象）：

```java
Class<? extends UserType> dynamicUserType = new ByteBuddy()
  .subclass(UserType.class)
    .method(not(isDeclaredBy(Object.class)))
    .intercept(MethodDelegation.toField("interceptor")); 
```

