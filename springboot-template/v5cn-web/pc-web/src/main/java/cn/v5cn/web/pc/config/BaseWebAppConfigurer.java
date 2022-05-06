package cn.v5cn.web.pc.config;

import cn.v5cn.web.common.rwv.ResponseResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ZYW
 */
@Configuration
public class BaseWebAppConfigurer implements WebMvcConfigurer {



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        // 添加Controller返回值包装拦截器
        registry.addInterceptor(responseResultInterceptor());
    }

    @Bean
    public ResponseResultInterceptor responseResultInterceptor() {
        return new ResponseResultInterceptor();
    }
}
