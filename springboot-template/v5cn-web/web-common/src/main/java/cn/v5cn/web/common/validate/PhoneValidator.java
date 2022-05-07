package cn.v5cn.web.common.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 手机号校验器
 * @author zyw
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    /**
     * 手机号正则表达式
     */
    private static final String REGEXP_PHONE = "^1[3456789]\\d{9}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(Objects.isNull(value)) {
            return true;
        }

        return Pattern.matches(REGEXP_PHONE, value);
    }
}
