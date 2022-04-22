package cn.v5cn.springboot.redisson.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-04-11 13:54
 */
@Component
public class Lock2 {

    @Autowired
    private RedissonClient redisson;

    @Async
    public void lock() {
        RLock lock = redisson.getLock("lock-lock");
        System.out.println("------------------------------Lock2等待获取锁");
        lock.lock();
        try {
            System.out.println("Lock2-进入锁: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS")));
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("Lock2-解锁完成:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS")));
        }
    }
}
