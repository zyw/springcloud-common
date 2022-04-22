package cn.v5cn.others.casfaa;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CAS和FAA的操作系统原语
 * @author ZYW
 * @version 1.0
 * @date 2019-09-17 22:01
 */
public class CasFaa3 {
    public static void main(String[] args) throws InterruptedException {
        long[] balance = {0L};
        int count = 10000;
        ReentrantLock lock = new ReentrantLock();
        ExecutorCompletionService<String> threadPollService = new ExecutorCompletionService<>(Executors.newFixedThreadPool(100));
        CasFaa3 casFaa = new CasFaa3();
        for(int i = 0; i < count; i++) {
            threadPollService.submit(() -> {
                casFaa.transfer(balance,1,lock);
                return "";
            });
        }
        for(int i = 0; i < count; i++) {
            threadPollService.take().isDone();
        }

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
