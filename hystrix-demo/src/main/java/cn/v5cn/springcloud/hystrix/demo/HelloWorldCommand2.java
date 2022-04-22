package cn.v5cn.springcloud.hystrix.demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  使用Fallback() 提供降级策略
 *  NOTE: 除了HystrixBadRequestException异常之外，所有从run()方法抛出的异常都算作失败，并触发降级getFallback()和断路器逻辑。
 *  HystrixBadRequestException: 用在非法参数或非系统故障异常等不应触发回退逻辑的场景。
 *  用提供的参数或状态表示错误的异常，而不是执行失败。
 *  与HystrixCommand抛出的所有其他异常不同，这不会触发回退，不会计算故障指标，因此不会触发断路器。
 *  注意:只有当错误是由用户输入(如IllegalArgumentException)引起时，才应该使用这种方法，否则就会破坏容错和回退行为的目的。
 * </p>
 *
 * @author ZYW
 * @version v1.0.0
 * @date 2019-08-22 12:48
 */
public class HelloWorldCommand2 extends HystrixCommand<String> {

    private String name;

    protected HelloWorldCommand2(String name) {
        //依赖分组:CommandGroup
        //命令分组用于对依赖操作分组,便于统计,汇总等.
        //NOTE: CommandGroup是每个命令最少配置的必选参数，在不指定ThreadPoolKey的情况下，字面值用于对不同依赖的线程池/信号区分.
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(500)));
        this.name = name;
    }

    /*
     * 依赖命名:CommandKey
     * NOTE: 每个CommandKey代表一个依赖抽象,相同的依赖要使用相同的CommandKey名称。依赖隔离的根本就是对相同CommandKey的依赖做隔离.
     * /
     /*
    public HelloWorldCommand2(String name) { 
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                // HystrixCommandKey工厂定义依赖名称
                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld")));
        this.name = name;
    }*/

    /**
     * 线程池/信号:ThreadPoolKey
     * NOTE: 当对同一业务依赖做隔离时使用CommandGroup做区分,但是对同一依赖的不同远程调用如(一个是redis 一个是http),可以使用HystrixThreadPoolKey做隔离区分.
     * 最然在业务上都是相同的组，但是需要在资源上做隔离时，可以使用HystrixThreadPoolKey区分.
     */
    /*public HelloWorldCommand2(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld"))
                // 使用HystrixThreadPoolKey工厂定义线程池名称 
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HelloWorldPool")));
        this.name = name;
    }*/

    @Override
    protected String run() throws Exception {
        //sleep 1 秒,调用会超时  17        
        TimeUnit.MILLISECONDS.sleep(1000);
        return "Hello " + name +" thread:" + Thread.currentThread().getName();
    }

    @Override
    protected String getFallback() {
        return "exeucute Falled";
    }

    public static void main(String[] args) {
        HelloWorldCommand2 command2 = new HelloWorldCommand2("test-Fallback");
        String result = command2.execute();
        System.out.println("result=" + result);
    }
    /*
     * 运行结果：
     * result=exeucute Falled
     */
}
