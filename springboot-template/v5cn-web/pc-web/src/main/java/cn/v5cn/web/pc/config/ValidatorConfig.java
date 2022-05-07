package cn.v5cn.web.pc.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Properties;

/**
 * 配置Hibernate参数校验
 * @author zyw
 */
@Configuration
public class ValidatorConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        // 快速校验，只要有错马上返回
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    /**
     * 实体类字段校验国际化引入
     */
    @Bean
    public Validator validator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        //设置messages资源信息
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        // 多个用逗号分隔
        messageSource.setBasenames("classpath:/messages/validation/messages");
        // 设置字符集编码
        messageSource.setDefaultEncoding("UTF-8");
        validator.setValidationMessageSource(messageSource);
        // 设置验证相关参数
        Properties properties = new Properties();
        // 快速失败，只要有错马上返回
        properties.setProperty("hibernate.validator.fail_fast", "true");
        validator.setValidationProperties(properties);

//        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
//                .configure()
//                .addProperty("hibernate.validator.fail_fast", "true")
//                .buildValidatorFactory();
//        return validatorFactory.getValidator();
        return validator;
    }
}
