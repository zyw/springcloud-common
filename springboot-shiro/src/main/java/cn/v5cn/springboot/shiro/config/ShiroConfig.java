package cn.v5cn.springboot.shiro.config;

import cn.v5cn.springboot.shiro.CustomRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZYW
 * @version 1.0
 * @date 2019-09-01 21:27
 */
@Configuration
public class ShiroConfig {

    /**
     * 注入自定义的realm，告诉shiro如何获取用户信息来做登录或权限控制
     * @return CustomRealm
     */
    @Bean
    public Realm realm() {
        return new CustomRealm();
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();

        //添加自定义的filter


        /*// 由于demo1展示统一使用注解做访问控制，所以这里配置所有请求路径都可以匿名访问
        chain.addPathDefinition("/**", "anon"); // all paths are managed via annotations

        // 这另一种配置方式。但是还是用上面那种吧，容易理解一点。
        // or allow basic authentication, but NOT require it.
        // chainDefinition.addPathDefinition("/**", "authcBasic[permissive]");*/

        //哪些请求可以匿名访问
        chain.addPathDefinition("/user/login", "anon");
        chain.addPathDefinition("/page/401", "anon");
        chain.addPathDefinition("/page/403", "anon");
        chain.addPathDefinition("/t5/hello", "anon");
        chain.addPathDefinition("/t5/guest", "anon");

        //除了以上的请求外，其它请求都需要登录
//        chain.addPathDefinition("/**", "authc");

        return chain;
    }

    /**
     * 解决spring aop和注解配置一起使用的bug。如果您在使用shiro注解配置的同时，引入了spring aop的starter，
     * 会有一个奇怪的问题，导致shiro注解的请求，不能被映射，需加入以下配置：
     * @return
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
