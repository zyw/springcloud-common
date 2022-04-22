package cn.v5cn.springboot.redisson.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分布式锁的请求发起者
 * @author ZYW
 * @version 1.0
 * @date 2020-04-11 13:53
 */
@Service
public class DistributedLockService {

    @Autowired
    private Lock1 lock1;
    @Autowired
    private Lock2 lock2;

    public void lockService() {
        System.out.println("调用lock1");
        lock1.lock();
        System.out.println("调用lock2");
        lock2.lock();
    }
}
