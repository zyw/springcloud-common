package cn.v5cn.springcloud.authserver.config;

import cn.v5cn.springcloud.authserver.exception.CustomWebResponseExceptionTranslator;
import cn.v5cn.springcloud.authserver.mapper.SysClientDetailsMapper;
import cn.v5cn.springcloud.authserver.security.CustomAuthorizationTokenServices;
import cn.v5cn.springcloud.authserver.security.CustomRedisTokenStore;
import cn.v5cn.springcloud.authserver.security.CustomTokenEnhancer;
import cn.v5cn.springcloud.authserver.service.ClientDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String JWT_TOKE_KEY = "v5@cn#auth$";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SysClientDetailsMapper sysClientDetailsMapper;

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public TokenStore tokenStore() {
        return new CustomRedisTokenStore(connectionFactory);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new CustomTokenEnhancer();
        converter.setSigningKey(JWT_TOKE_KEY);
        return converter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                //允许表单提交
                .allowFormAuthenticationForClients()
                //允许检查Token
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .tokenServices(authorizationServerTokenServices())
                .accessTokenConverter(accessTokenConverter())
                //.userDetailsService(userDetailsService)
                .exceptionTranslator(webResponseExceptionTranslator());
        /*endpoints.setClientDetailsService(clientDetailsService(dataSource));*/
        // 配置TokenServices参数
        /*DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        //30天(int)
        tokenServices.setAccessTokenValiditySeconds(Long.valueOf(TimeUnit.DAYS.toSeconds(30)).intValue());
        endpoints.tokenServices(tokenServices);*/
    }

    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        CustomAuthorizationTokenServices customTokenServices = new CustomAuthorizationTokenServices();
        customTokenServices.setTokenStore(tokenStore());
        customTokenServices.setSupportRefreshToken(true);
        customTokenServices.setReuseRefreshToken(true);
        customTokenServices.setClientDetailsService(clientDetailsService());
        customTokenServices.setTokenEnhancer(accessTokenConverter());
        return customTokenServices;
    }

    @Bean
    public ClientDetailsService clientDetailsService(){
        return new ClientDetailsServiceImpl(sysClientDetailsMapper);
    }
}
