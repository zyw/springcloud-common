package cn.v5cn.cache2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/10 11:13 下午
 */
@EnableCaching
@SpringBootApplication
public class Cache2Application {
    public static void main(String[] args) {
        SpringApplication.run(Cache2Application.class, args);
    }
}
