package cn.v5cn.dynamic.proxy.jdk;

/**
 * JDK的动态代理实现，JDK动态代理是针对接口和实现类的代理
 * @author ZYW
 * @version 1.0
 * @date 2020-02-24 13:30
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理的接口类，InvocationHandler和proxy分开，没有添加泛型
 */
interface IHello {
    void say(String s);
}

//被代理类，可以对类进行增强
class RealHello implements IHello {

    @Override
    public void say(String s) {
        System.out.println("hello " + s);
    }
}

//增强器，必须继承自InvocationHandler接口
class HelloDelegate implements InvocationHandler {

    //被代理接口类型
    private IHello target;

    public HelloDelegate(IHello target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用前。。。。。");
        //target是被代理的接口实现类，所有需要实现类的对象。如果只是代理，
        // 不发起invoke就不需要接口实现对象。RPC就是这样实现，所以client端不需要指定实现类示例。
        Object invoke = method.invoke(target, args);
        System.out.println("调用后。。。。。");
        return invoke;
    }
}

public class DynamicProxy {
    public static void main(String[] args) {
        IHello iHello = enhanceHello(new RealHello());
        System.out.println("代理类名称：" + iHello.getClass().getName());
        System.out.println("代理类实现的接口 " + iHello.getClass().getInterfaces()[0].getName());
        iHello.say("张三");
    }

    public static IHello enhanceHello(IHello target) {
        return (IHello) Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(),new Class<?>[] {IHello.class},new HelloDelegate(target));
    }
}
