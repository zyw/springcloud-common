package cn.v5cn.springboot.redisson;

import cn.v5cn.springboot.redisson.controller.IndexController;
import cn.v5cn.springboot.redisson.service.SendReadMsg;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

@SpringBootApplication
@EnableAsync
public class RedissonDemoApplication {

    @Autowired
    private SendReadMsg sendReadMsg;

    @Autowired
    private RedissonClient redisson;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(RedissonDemoApplication.class, args);
        IndexController bean = applicationContext.getBean(IndexController.class);
        //bean.readMsg();
    }

    /**
     * 读取消息线程，使用一个线程就可以，启动是创建一个读取队列里面消息的线程。
     */
    @PostConstruct
    private void msgRead() {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("zyw-thread-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,
                1, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                nameThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        executor.submit(() -> {
            while (true) {
                RLock lock = null;
                try {
                    String msg = sendReadMsg.readMsg();
                    lock = redisson.getLock("lock-lock");
                    System.out.println("------------------------------msgRead等待获取锁: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS")));
                    lock.lock();
                    System.out.println("msgRead进入锁: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS")));
                    String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    System.out.println("收到消息：" + format + " MSG: " + msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(lock != null) {
                        lock.unlock();
                        System.out.println("msgRead解锁完成: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss:SSS")));
                    }
                }
            }
        });
    }
}
