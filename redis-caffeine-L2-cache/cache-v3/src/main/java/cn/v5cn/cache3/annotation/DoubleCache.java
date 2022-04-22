package cn.v5cn.cache3.annotation;

import java.lang.annotation.*;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/11 9:34 下午
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoubleCache {

    String cacheName();

    String key(); //支持springEl表达式

    long l2Timeout() default 120;

    CacheType type() default CacheType.FULL;
}
