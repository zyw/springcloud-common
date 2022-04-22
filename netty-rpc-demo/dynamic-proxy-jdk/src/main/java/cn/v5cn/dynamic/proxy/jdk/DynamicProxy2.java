package cn.v5cn.dynamic.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理，
 * @author ZYW
 * @version 1.0
 * @date 2020-02-24 13:50
 */
//代理接口
interface IHello2 {
    void say(String s);
}

//被代理接口实现类
class ReadHello2 implements IHello2 {

    @Override
    public void say(String s) {
        System.out.println("hello 2 " + s);
    }
}

class ProxyDynamic<T> implements InvocationHandler {

    private T target;

    public ProxyDynamic(T target) {
        this.target = target;
    }

    public T newProxyInstance(ClassLoader loader, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(loader,interfaces,this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("增强前。。。。。。。");
        Object result = method.invoke(target, args);
        System.out.println("增强后。。。。。。。");
        return result;
    }
}

/**
 * @author zhuyanwei
 */
public class DynamicProxy2 {
    public static void main(String[] args) {
        IHello2 hello2 = new ProxyDynamic<>(new ReadHello2()).newProxyInstance(DynamicProxy2.class.getClassLoader(), new Class<?>[]{IHello2.class});

        hello2.say("李四");
    }
}
