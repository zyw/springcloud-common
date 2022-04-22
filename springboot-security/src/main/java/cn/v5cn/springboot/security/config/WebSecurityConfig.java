package cn.v5cn.springboot.security.config;

import cn.v5cn.springboot.security.filter.MyUsernamePasswordAuthenticationFilter;
import cn.v5cn.springboot.security.handler.MyAccessDeniedHandler;
import cn.v5cn.springboot.security.handler.MyAuthenticationFailureHandler;
import cn.v5cn.springboot.security.handler.MyAuthenticationSuccessHandler;
import cn.v5cn.springboot.security.handler.MyLogoutSuccessHandler;
import cn.v5cn.springboot.security.jwt.JwtAuthenticationEntryPoint;
import cn.v5cn.springboot.security.jwt.JwtAuthenticationTokenFilter;
import cn.v5cn.springboot.security.util.SkipPathRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;
import java.util.List;

/***
 *
 * @FileName: WebSecurityConfig
 * @Company:
 * @author    ljy
 * @Date      2018年05月11日
 * @version   1.0.0
 * @remark:   web 安全性配置
 * @explain   当用户登录时会进入此类的loadUserByUsername方法对用户进行验证，
 *             验证成功后会被保存在当前回话的principal对象中
 *             系统获取当前登录对象信息方法 WebUserDetails webUserDetails =
 *             (WebUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 *
 */
@Configuration
@EnableWebSecurity //启动web安全性
//开启方法级的权限注解  性设置后控制器层的方法前的@PreAuthorize("hasRole('admin')") 注解才能起效
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    // 不需要认证的接口
    @Value("${com.example.oauth.security.antMatchers}")
    private String antMatchers;

    /**
     * 配置Spring Security的Filter链
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/favicon.ico");
        web.ignoring().antMatchers("/error");
        super.configure(web);
    }

    /**
     * 置user-detail服务
     *
     * 方法描述
     * accountExpired(boolean)                定义账号是否已经过期
     * accountLocked(boolean)                 定义账号是否已经锁定
     * and()                                  用来连接配置
     * authorities(GrantedAuthority...)       授予某个用户一项或多项权限
     * authorities(List)                      授予某个用户一项或多项权限
     * authorities(String...)                 授予某个用户一项或多项权限
     * disabled(boolean)                      定义账号是否已被禁用
     * withUser(String)                       定义用户的用户名
     * password(String)                       定义用户的密码
     * roles(String...)                       授予某个用户一项或多项角色
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        // 配置指定用户权限信息  通常生产环境都是从数据库中读取用户权限信息而不是在这里配置
        //auth.inMemoryAuthentication().withUser("username1").password("123456").roles("USER").and().withUser("username2").password("123456").roles("USER","AMDIN");

        // ****************   基于数据库中的用户权限信息 进行认证
        //指定密码加密所使用的加密器为 bCryptPasswordEncoder()
        //需要将密码加密后写入数据库
        // myUserDetailService 类中获取了用户的用户名、密码以及是否启用的信息，查询用户所授予的权限，用来进行鉴权，查询用户作为群组成员所授予的权限
        auth.userDetailsService(myUserDetailService).passwordEncoder(bCryptPasswordEncoder());
        //不删除凭据，以便记住用户
        auth.eraseCredentials(false);
    }

    /**
     *  解决 无法直接注入 AuthenticationManager
     * @return
     * @throws Exception
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置如何通过拦截器保护请求
     * 指定哪些请求需要认证，哪些请求不需要认证，以及所需要的权限
     * 通过调用authorizeRequests()和anyRequest().authenticated()就会要求所有进入应用的HTTP请求都要进行认证
     *
     * 方法描述
     * anonymous()                                        允许匿名用户访问
     * authenticated()                                    允许经过认证的用户访问
     * denyAll()                                          无条件拒绝所有访问
     * fullyAuthenticated()                如果用户是完整的话（不是通过Remember-me功能认证的），就允许访问
     * hasAnyAuthority(String...)                 如果用户具备给定权限中的某一个的话，就允许访问
     * hasAnyRole(String...)                    如果用户具备给定角色中的某一个的话，就允许访问
     * hasAuthority(String)                     如果用户具备给定权限的话，就允许访问
     * hasIpAddress(String)                    如果请求来自给定IP地址的话，就允许访问
     * hasRole(String)                        如果用户具备给定角色的话，就允许访问
     * not()                               对其他访问方法的结果求反
     * permitAll()                           无条件允许访问
     * rememberMe()                          如果用户是通过Remember-me功能认证的，就允许访问
     *
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("不需要认证的URL: " + antMatchers);
        http.csrf().disable()
                // 基于token，所以不需要session  如果基于session 则不要使用这段代码
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //对请求进行认证  url认证配置顺序为：
                // 1.先配置放行不需要认证的 permitAll()
                // 2.然后配置 需要特定权限的 hasRole()
                // 3.最后配置 anyRequest().authenticated()
                .authorizeRequests()
                // 所有 /oauth/v1/api/login/ 请求的都放行 不做认证即不需要登录即可访问
                .antMatchers(antMatchers.split(",")).permitAll()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("oauth/**").permitAll()
                // 其他请求都需要进行认证,认证通过够才能访问
                // 待考证：如果使用重定向
                // httpServletRequest.getRequestDispatcher(url).forward(httpServletRequest,httpServletResponse); 重定向跳转的url不会被拦截（即在这里配置了重定向的url需要特定权限认证不起效），但是如果在Controller 方法上配置了方法级的权限则会进行拦截
                .anyRequest().authenticated()
                .and().exceptionHandling()
                // 认证配置当用户请求了一个受保护的资源，但是用户没有通过登录认证，则抛出登录认证异常，MyAuthenticationEntryPointHandler类中commence()就会调用
                .authenticationEntryPoint(myAuthenticationEntryPoint())
                //用户已经通过了登录认证，在访问一个受保护的资源，但是权限不够，则抛出授权异常，MyAccessDeniedHandler类中handle()就会调用
                .accessDeniedHandler(myAccessDeniedHandler())
                .and()
                .formLogin()
                // 此登录url 和Controller 无关系
                .loginProcessingUrl("")
                // username参数名称 后台接收前端的参数名
                .usernameParameter("username")
                //登录密码参数名称 后台接收前端的参数名
                .passwordParameter("password")
                //登录成功跳转路径
                .successForwardUrl("/")
                //登录失败跳转路径
                .failureUrl("/")
                // 登录页面路径
                .loginPage("/")
                .permitAll()
                ////登录成功后 MyAuthenticationSuccessHandler类中onAuthenticationSuccess（）被调用
                .successHandler(myAuthenticationSuccessHandler())
                //登录失败后 MyAuthenticationFailureHandler 类中onAuthenticationFailure（）被调用
                .failureHandler(myAuthenticationFailureHandler())
                .and()
                .logout()
                //退出系统url
                .logoutUrl("/")
                //退出系统后的url跳转
                .logoutSuccessUrl("/")
                //退出系统后的 业务处理
                .logoutSuccessHandler(myLogoutSuccessHandler())
                .permitAll()
                .invalidateHttpSession(true)
                .and()
                //登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
                // 勾选Remember me登录会在PERSISTENT_LOGINS表中，生成一条记录
                .rememberMe()
                .tokenValiditySeconds(3600);
//        //添加JWT过滤器 除/login其它请求都需经过此过滤器
//        http.addFilter(new JwtAuthenticationTokenFilter());
        // 加入自定义UsernamePasswordAuthenticationFilter替代原有Filter
        http.addFilterAt(myUsernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
        //在 beforeFilter 之前添加 自定义 filter
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        // 添加JWT filter 验证其他请求的Token是否合法
        http.addFilterBefore(authenticationTokenFilterBean(), OncePerRequestFilter.class);
        // 禁用缓存
        http.headers().cacheControl();
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin().and().httpBasic();
    }

    /**
     * 密码加密方式
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注册  登录认证 bean
     * @return
     */
    @Bean
    public AuthenticationEntryPoint myAuthenticationEntryPoint(){

        //return new MyAuthenticationEntryPointHandler();
        return new JwtAuthenticationEntryPoint();
    }

    /**
     * 注册  认证权限不足处理 bean
     * @return
     */
    @Bean
    public AccessDeniedHandler myAccessDeniedHandler(){
        return new MyAccessDeniedHandler();
    }

    /**
     * 注册  登录成功 处理 bean
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler();
    }

    /**
     *  注册 登录失败 处理 bean
     * @return
     */
    @Bean
    public AuthenticationFailureHandler myAuthenticationFailureHandler(){
        return new MyAuthenticationFailureHandler();
    }
    /**
     * 注册 退出系统成功 处理bean
     * @return
     */
    @Bean
    public LogoutSuccessHandler myLogoutSuccessHandler(){
        return new MyLogoutSuccessHandler();
    }

    /**
     * 注册jwt 认证
     * @return
     * @throws Exception
     */
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        // JwtAuthenticationTokenFilter 过滤器被配置为跳过这个点：/auth/v1/api/login/retrieve/pwd 和 /auth/v1/api/login/entry 不进行token 验证. 通过 SkipPathRequestMatcher 实现 RequestMatcher 接口来实现。
        List<String> pathsToSkip = Arrays.asList("/auth/v1/api/login/retrieve/pwd","/auth/v1/api/login/entry","/auth/v1/api/login/enter");  //不需要token 验证的url
        String processingPath = "/auth/v1/api/**"; //　需要验证token　的url
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, processingPath);
        return new JwtAuthenticationTokenFilter(matcher);
    }

    /**
     * 验证登录验证码
     * @return
     * @throws Exception
     */
    @Bean
    public UsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        return new MyUsernamePasswordAuthenticationFilter(authenticationManagerBean(),myAuthenticationSuccessHandler(),myAuthenticationFailureHandler());
    }
}
