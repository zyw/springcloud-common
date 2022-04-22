package cn.v5cn.dynamic.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * 委托方法调用
 * @author ZYW
 * @version 1.0
 * @date 2020-03-02 22:58
 */

public class ObjectProxy3 {
    public void target() throws IllegalAccessException, InstantiationException {
        String hello = new ByteBuddy()
                .subclass(Source.class)
                .method(ElementMatchers.named("hello"))
                .intercept(MethodDelegation.to(Target.class))
                .make()
                .load(this.getClass().getClassLoader())
                .getLoaded()
                .newInstance()
                .hello("World1111");
        System.out.println("result: " + hello);
    }
    public static void main(String[] args) {
        try {
            new ObjectProxy3().target();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
