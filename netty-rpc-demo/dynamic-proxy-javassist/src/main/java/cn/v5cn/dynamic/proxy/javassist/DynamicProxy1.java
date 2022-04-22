package cn.v5cn.dynamic.proxy.javassist;

import javassist.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 使用javassist动态代理接口，不需要接口类的实现，RPC的实现方式。
 *
 * @author ZYW
 * @version 1.0
 * @date 2020-03-01 18:11
 */

//代理接口
interface HelloService {
    /**
     * 需要代理的接口方法
     * @param name
     * @return
     */
    String say(String name);
}

//使用代码拼接的方式创建接口代理类
class MyProxy<T> {

    private static AtomicInteger counter = new AtomicInteger();

    private Class<T> t;

    public MyProxy(Class<T> t) {
        this.t = t;
    }

    public T getProxy() {
        try {
            ClassPool classPool = ClassPool.getDefault();

            //创建代理类
            CtClass ctClass = classPool.makeClass(getTypeStr(t) + "_proxy_" + counter.getAndIncrement());
            //指定代理类实现的接口
            ctClass.addInterface(classPool.get(t.getName()));

            Method[] methods = t.getMethods();
            Field[] fields = t.getFields();
            StringBuilder sb = new StringBuilder(512);
            for (Method method : methods) {
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<?> returnType = method.getReturnType();

                sb.append(Modifier.toString(method.getModifiers()).replace("abstract",""))
                        .append(getTypeStr(returnType))
                        .append(" ").append(methodName).append("(");

                int c = 0;
                for(Class<?> p : parameterTypes) {
                    sb.append(" ").append(p.getCanonicalName()).append(" arg").append(c).append(" ,");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");

                Class<?>[] exceptions = method.getExceptionTypes();
                if (exceptions.length > 0) {
                    sb.append(" throws ");
                    for (Class<?> exception : exceptions) {
                        sb.append(exception.getCanonicalName()).append(" ,");
                    }
                    sb = sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("{");

                sb.append("System.out.println(arg0);return \"Hello \" + arg0; }");

                String clazzStr = sb.toString();

                System.out.println("拼接的类：" + clazzStr);

                ctClass.addMethod(CtMethod.make(clazzStr, ctClass));

            }

            Class<?> aClass = ctClass.toClass();
            return (T) aClass.newInstance();
        } catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取包名称
    public String getPackageName() {
        Package aPackage = t.getPackage();
        return aPackage.getName();
    }

    public static String getTypeStr(Class<?> clazz) {
        String typeStr = "";
        if (clazz.isArray()) {
            String name = clazz.getName(); // 原始名字：[Ljava.lang.String;
            typeStr = jvmNameToCanonicalName(name); // java.lang.String[]
        } else {
            typeStr = clazz.getName();
        }
        return typeStr;
    }

    /**
     * JVM描述转通用描述
     *
     * @param jvmName 例如 [I;
     * @return 通用描述 例如 int[]
     */
    public static String jvmNameToCanonicalName(String jvmName) {
        boolean isArray = jvmName.charAt(0) == '[';
        if (isArray) {
            String cnName = ""; // 计数，看上几维数组
            int i = 0;
            for (; i < jvmName.length(); i++) {
                if (jvmName.charAt(i) != '[') {
                    break;
                }
                cnName += "[]";
            }
            String componentType = jvmName.substring(i, jvmName.length());
            if ("Z".equals(componentType)) {
                cnName = "boolean" + cnName;
            } else if ("B".equals(componentType)) {
                cnName = "byte" + cnName;
            } else if ("C".equals(componentType)) {
                cnName = "char" + cnName;
            } else if ("D".equals(componentType)) {
                cnName = "double" + cnName;
            } else if ("F".equals(componentType)) {
                cnName = "float" + cnName;
            } else if ("I".equals(componentType)) {
                cnName = "int" + cnName;
            } else if ("J".equals(componentType)) {
                cnName = "long" + cnName;
            } else if ("S".equals(componentType)) {
                cnName = "short" + cnName;
            } else {
                cnName = componentType.substring(1, componentType.length() - 1) + cnName; // 对象的 去掉L
            }
            return cnName;
        }
        return jvmName;
    }
}

//使用其他方式
class MyProxy2<T> {
    private Class<T> t;

    private static AtomicInteger counter = new AtomicInteger();

    public MyProxy2(Class<T> t) {
        this.t = t;
    }

    public T getProxy() {
        try {
            ClassPool pool = ClassPool.getDefault();
            //创建代理类
            CtClass ctClass = pool.makeClass(MyProxy.getTypeStr(t) + "_proxy2_" + counter.getAndIncrement());

            //设置代理类的接口
            CtClass interf = pool.getCtClass(getPackageName() + "." + t.getSimpleName());//获取代理对象的接口类
            CtClass[] interfaces = new CtClass[]{interf};
            ctClass.setInterfaces(interfaces);

            CtMethod[] methods = interf.getDeclaredMethods();
            CtField[] fields = interf.getDeclaredFields();

            for (CtMethod m : methods) {
                String name = m.getName();
                CtMethod cm = new CtMethod(m.getReturnType(),name,m.getParameterTypes(),ctClass);
                // 使用($w)$1来获得方法的第一个参数。
                cm.setBody("{System.out.println(\"hello \"+($w)$1); return \"Hello \" + ($w)$1;}");

                ctClass.addMethod(cm);
            }
            Class aClass = ctClass.toClass();
            return (T) aClass.newInstance();

        } catch (NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取包名
    public String getPackageName(){
        Package aPackage = t.getPackage();
        return aPackage.getName();
    }
}

public class DynamicProxy1 {
    public static void main(String[] args) {
        MyProxy<HelloService> proxy = new MyProxy<>(HelloService.class);

        String say = proxy.getProxy().say("张三");
        System.out.println(say);

        MyProxy2<HelloService> proxy2 = new MyProxy2<>(HelloService.class);

        String say2 = proxy2.getProxy().say("李四");
        System.out.println(say2);


    }
}
