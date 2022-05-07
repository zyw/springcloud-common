package cn.v5cn.web.common.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证手机号是否合法
 * @author ludangxin
 * @date 2021/8/7
 */
@Documented
@Constraint(validatedBy = { PhoneValidator.class })
@Target({METHOD, FIELD, ANNOTATION_TYPE,CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface Phone {

    //默认错误消息
    String message() default "不是一个合法的手机号";

    // 分组
    Class<?>[] groups() default {};

    //载荷 将某些元数据信息与给定的注解声明相关联的方法
    Class<? extends Payload>[] payload() default {};

    // 指定多个时使用
    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Phone[] value();
    }

}
