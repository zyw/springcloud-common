package cn.v5cn.others.futures;

import java.util.concurrent.CompletableFuture;

/**
 * 创建 CompletableFuture 方法
 * 1. thenCompose 平行处理两个
 * 2. thenCombine 聚合两个stage的result
 * 3. thenAccept 直接消费
 * 4. thenRun 直接执行
 * 5. theApply
 * @author zyw
 */
public class CompletableFutureDemo3 {
    public static void main(String[] args) {
        // 1. 使用new方法创建
        CompletableFuture<String> cf1 = new CompletableFuture<>();
        // 2. 调用supplyAsync静态方法去异步执行，返回一个带有result的CompleteableFuture
        CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync(() -> {
           return "supplyAsyncFuture";
        });
        // 3. 静态方法runAsync异步执行后返回一个没有result的CompleteableFuture
        CompletableFuture<Void> runAsyncCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println("runAsyncCompletableFuture");
        });
        // 4. 使用静态方法completedFuture，直接返回一个完成状态的CompleteableFuture
        CompletableFuture<String> completedFuture = CompletableFuture.completedFuture("completedFuture");
    }
}
