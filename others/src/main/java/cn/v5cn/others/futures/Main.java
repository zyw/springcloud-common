package cn.v5cn.others.futures;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author ZYW
 * @version 1.0
 * @date 2019-09-14 19:32
 */
public class Main {
    private static Random rand = new Random();
    private static long t = System.currentTimeMillis();

    static int getMoreDate() {
        System.out.println("begin to start compute");
        try{
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        System.out.println("end to start compute. passed " + (System.currentTimeMillis() - t)/1000 + " seconds");
        return rand.nextInt(1000);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(Main::getMoreDate);

        Future<Integer> f = future.whenComplete((v, e) -> {
            System.out.println(v);
            System.out.println(e);
        });

        System.out.println(f.get());
        System.in.read();
    }
}
