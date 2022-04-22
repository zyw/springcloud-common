package cn.v5cn.springcloud.hystrix.demo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 *  使用命令模式封装依赖逻辑
 *  note:
 *  <b>
 *   异步调用使用 command.queue()get(timeout, TimeUnit.MILLISECONDS);
 *   同步调用使用command.execute() 等同于 command.queue().get();
 *  </b>
 * </p>
 *
 * @author ZYW
 * @version v1.0.0
 * @date 2019-08-22 11:16
 */
public class HelloWorldCommand extends HystrixCommand<String> {

    private String name;

    protected HelloWorldCommand(String name) {
        //最少配置:指定命令组名(CommandGroup)
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        // 依赖逻辑封装在run()方法中
        return "Hello " + name + " thread: " + Thread.currentThread().getName();
    }

    /**
     * 注册异步事件回调执行
     */
    public void regCall() {
        Observable<String> fs = this.observe();
        fs.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                //执行结果处理,result 为HelloWorldCommand返回的结果
                //用户对结果做二次处理.
                System.out.println("result=" + s);
            }
        });
        //注册完整执行生命周期事件
        fs.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                // onNext/onError完成之后最后回调
                System.out.println("execute onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                // 当产生异常时回调
                System.out.println("onError " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                // 获取结果后回调
                System.out.println("onNext: " + v);
            }
        });
    }

    // 调用实例
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        // 每个Command对象只能调用一次,不可以重复调用,
        // 重复调用对应异常信息:This instance can only be executed once. Please instantiate a new instance.    
        HelloWorldCommand helloWorldCommand = new HelloWorldCommand("Synchronous-hystrix");
        String result = helloWorldCommand.execute();
        // 使用execute()同步调用代码,效果等同于:helloWorldCommand.queue().get();
        System.out.println("result=" + result);

        helloWorldCommand = new HelloWorldCommand("Asynchronous-hystrix");
        //异步调用,可自由控制获取结果时机,
        Future<String> future = helloWorldCommand.queue();
        //get操作不能超过command定义的超时时间,默认:1秒
        result = future.get(100, TimeUnit.MILLISECONDS);
        System.out.println("result=" + result);
        System.out.println("mainThread=" + Thread.currentThread().getName());

        System.out.println("#######################################################");

        //回调执行
        helloWorldCommand = new HelloWorldCommand("observe");
        helloWorldCommand.regCall();
    }

//    运行结果: run()方法在不同的线程下执行 
//    result=Hello Synchronous-hystrix thread: hystrix-ExampleGroup-1
//    result=Hello Asynchronous-hystrix thread: hystrix-ExampleGroup-2
//    mainThread=main
//    #######################################################
//    result=Hello observe thread: hystrix-ExampleGroup-3
//    onNext: Hello observe thread: hystrix-ExampleGroup-3
//    execute onCompleted
}
