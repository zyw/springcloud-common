package cn.v5cn.rcl2cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/10 10:21 下午
 */
@Configuration
public class CaffeineConfig {

    public Cache<String,Object> caffeineCache() {
        return Caffeine.newBuilder()
                .initialCapacity(128) // 初始大小
                .maximumSize(1024) // 最大数量
                .expireAfterWrite(60, TimeUnit.SECONDS) // 过期时间
                .build();
    }
}
