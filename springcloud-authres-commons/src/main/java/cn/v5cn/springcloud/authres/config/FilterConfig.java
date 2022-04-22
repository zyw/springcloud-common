package cn.v5cn.springcloud.authres.config;

import cn.v5cn.springcloud.authres.filter.AuthorizationFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class FilterConfig {

    public FilterRegistrationBean<AuthorizationFilter> filterRegistrationBean(){
        FilterRegistrationBean<AuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authorizationFilter());
        registrationBean.setOrder(0);
        return registrationBean;
    }

    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter();
    }
}
