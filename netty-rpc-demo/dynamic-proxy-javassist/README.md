# javassist
`javassist`是使用最广泛的动态代理开源库。下面我们使用`javassist`实现一个无需定义接口就能增强原始方法的例子。
![javassist](./img/1.png)

看起来和原生`jdk`提供的动态代理区别并不大，达到的效果是一样的。只不过这里要简单了很多，省去了接口类的定义。
`javassist`的`ProxyFactory`还提供了方法过滤器，它可以选择性地对特定方法进行增强。