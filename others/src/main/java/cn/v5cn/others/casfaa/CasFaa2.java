package cn.v5cn.others.casfaa;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CAS和FAA的操作系统原语
 * @author ZYW
 * @version 1.0
 * @date 2019-09-17 22:01
 */
public class CasFaa2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long[] balance = {0L};
        int count = 10000;
        ReentrantLock lock = new ReentrantLock();
        List<CompletableFuture> futures = Lists.newArrayList();
        CasFaa2 casFaa = new CasFaa2();
        for(int i = 0; i < count; i++) {
            futures.add(CompletableFuture.runAsync(() -> casFaa.transfer(balance,1,lock)));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).get();

        System.out.println("转账结果：" + balance[0]);
    }

    public void transfer(long[] balance, int amount, ReentrantLock lock) {
        try {
            lock.lock();
            balance[0] = balance[0] + amount;
        } finally {
            lock.unlock();
        }
    }
}
