package cn.v5cn.dynamic.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * java.lang.Object类的代理，重写toString方法
 * @author ZYW
 * @version 1.0
 * @date 2020-03-01 21:36
 */
public class ObjectProxy {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class<?> dynamicType = new ByteBuddy().subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(ObjectProxy.class.getClassLoader())
                .getLoaded();

        System.out.println("所在包：" + dynamicType.getPackage().getName());
        System.out.println("类名称：" + dynamicType.getName());

        System.out.println(dynamicType.newInstance().toString());

//        没有指定类名称，byte buddy会随机生成一个类名称，规则如下输出。
//        包名称生成规则是，如果父类不是java.lang包的类，那么包名称就是跟父类在同一个包下，如果是java.lang包的类，
//        规则如下输出，在包名称前会添加net.bytebuddy.renamed前缀
//        上面输出：
//        所在包：net.bytebuddy.renamed.java.lang
//        类名称：net.bytebuddy.renamed.java.lang.Object$ByteBuddy$15EdYfqM
//        Hello World!
    }
}
