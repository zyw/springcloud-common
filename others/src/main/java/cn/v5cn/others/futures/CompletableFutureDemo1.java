package cn.v5cn.others.futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author ZYW
 * @version 1.0
 * @date 2019-09-14 13:58
 */
public class CompletableFutureDemo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i = 1/0;
            return 100;
        });

        //future.join();
        future.get();
    }
}
