package cn.v5cn.web.common.rwv;

import java.lang.annotation.*;

/**
 * 标识改类中的所有方法或者指定方法方法的返回值需要进行包装
 * @author zyw
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ResponseResult {
}
