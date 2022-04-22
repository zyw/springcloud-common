package cn.v5cn.cache2.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/11 8:52 下午
 */
@Configuration
public class CacheManagerConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                    .initialCapacity(128)// 初始大小
                    .maximumSize(1024)   // 最大数量
                    .expireAfterWrite(60, TimeUnit.SECONDS));
        return cacheManager;
    }
}
