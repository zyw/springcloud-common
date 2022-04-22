package cn.v5cn.security2.security.config;

import cn.v5cn.security2.security.handler.MyAccessDeniedHandler;
import cn.v5cn.security2.security.util.SkipPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import cn.v5cn.security2.security.jwt.JwtAuthenticationEntryPoint;
import cn.v5cn.security2.security.service.UserInfoService;
import cn.v5cn.security2.security.jwt.JwtRequestFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 21:39
 */
@Configuration
@EnableWebSecurity
@ConfigurationProperties(prefix = "jwt")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    // 不需要认证的接口
    private List<String> antMatchers;

    public void setAntMatchers(List<String> antMatchers) {
        this.antMatchers = antMatchers;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //配置AuthenticationManager，以便它知道从何处加载匹配凭据的用户使用BCryptPasswordEncoder
        auth.userDetailsService(userInfoService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 在这个例子中我们不需要CSRF
        httpSecurity.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //对请求进行认证  url认证配置顺序为：
                // 1.先配置放行不需要认证的 permitAll()
                // 2.然后配置 需要特定权限的 hasRole()
                // 3.最后配置 anyRequest().authenticated()
                .authorizeRequests()
                .antMatchers(antMatchers.toArray(new String[0])).permitAll()
                // 所有其他请求都需要经过身份验证
                .anyRequest().authenticated()
                .and()
                // 确保我们使用无状态会话；会话不会用于存储用户的状态。
                .exceptionHandling()
                // 认证配置当用户请求了一个受保护的资源，但是用户没有通过登录认证，则抛出登录认证异常，MyAuthenticationEntryPointHandler类中commence()就会调用
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //用户已经通过了登录认证，在访问一个受保护的资源，但是权限不够，则抛出授权异常，MyAccessDeniedHandler类中handle()就会调用
                .accessDeniedHandler(accessDeniedHandler);
        // 添加一个过滤器来验证每个请求的令牌
        httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        SkipPathRequestMatcher requestMatcher = new SkipPathRequestMatcher(this.antMatchers, "");
        return new JwtRequestFilter(requestMatcher);
    }

//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("000000"));
//    }
}
