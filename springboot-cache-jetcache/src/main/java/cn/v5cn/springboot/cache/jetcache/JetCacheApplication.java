package cn.v5cn.springboot.cache.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "cn.v5cn.springboot.cache")
@EnableCreateCacheAnnotation
public class JetCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetCacheApplication.class, args);
    }

}
