package cn.v5cn.dynamic.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-02 22:49
 */
public class ObjectProxy2 {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Foo dynamicFoo = new ByteBuddy()
                .subclass(Foo.class)
                .method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("One!"))
                .method(named("foo")).intercept(FixedValue.value("Two!"))
                .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("Three!"))
                .make()
                .load(ObjectProxy2.class.getClassLoader())
                .getLoaded()
                .newInstance();


        System.out.println("bar() -> " + dynamicFoo.bar());
        System.out.println("foo() -> " + dynamicFoo.foo());
        System.out.println("foo(Object o) -> " + dynamicFoo.foo("Helloworld"));
    }
}
