package cn.v5cn.dynamic.proxy.javassist;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * javassist比JDK的动态代理要求要宽泛一下，接口不是必须的
 * @author ZYW
 * @version 1.0
 * @date 2020-02-24 19:56
 */
class RealHello {
    public void say(String s) {
        System.out.println("javassist: " + s);
    }
}

class HelloDelegate<T> implements MethodHandler {

    private T target;

    public HelloDelegate(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Method proceed, Object[] objects) throws Throwable {
        System.out.println("增强前。。。。。");
        Object result = method.invoke(target, objects);
        System.out.println("增强后。。。。。");
        return result;
    }
}

public class DynamicProxy {
    public static void main(String[] args) {
        RealHello hello = enhanceHello(new RealHello());
        hello.say("world");
    }

    public static <T> T enhanceHello(T target) {
        ProxyFactory proxy = new ProxyFactory();
        proxy.setSuperclass(RealHello.class);
        try {
            HelloDelegate<T> delegate = new HelloDelegate<>(target);
            // create方法传递两个空数组
            // 分别代表构造器的参数类型数组和构造器的参数实例数组
            return (T)proxy.create(new Class<?>[0],new Object[0],delegate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
