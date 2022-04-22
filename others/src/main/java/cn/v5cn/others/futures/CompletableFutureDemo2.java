package cn.v5cn.others.futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author ZYW
 * @version 1.0
 * @date 2019-09-14 13:58
 */
public class CompletableFutureDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return 1000;
        });

        CompletableFuture<String> f = future.thenApplyAsync(i -> i * 10).thenApply(i -> i.toString());
        System.out.println(f.get());
    }
}
