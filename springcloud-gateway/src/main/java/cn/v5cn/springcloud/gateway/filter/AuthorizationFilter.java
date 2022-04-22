package cn.v5cn.springcloud.gateway.filter;

import cn.v5cn.springcloud.gateway.http.HeaderEnhanceFilter;
import cn.v5cn.springcloud.gateway.properties.PermitAllUrlProperties;
import cn.v5cn.springcloud.gateway.security.CustomRemoteTokenServices;
import cn.v5cn.springcloud.gateway.security.OAuth2AccessToken;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * 全局鉴权过滤器
 * @author ZYW
 */
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);

    private final CustomRemoteTokenServices customRemoteTokenServices;

    private final HeaderEnhanceFilter headerEnhanceFilter;

    private PermitAllUrlProperties permitAllUrlProperties;

    public AuthorizationFilter(CustomRemoteTokenServices customRemoteTokenServices
            ,HeaderEnhanceFilter headerEnhanceFilter
            ,PermitAllUrlProperties permitAllUrlProperties) {
        this.customRemoteTokenServices = customRemoteTokenServices;
        this.headerEnhanceFilter = headerEnhanceFilter;
        this.permitAllUrlProperties = permitAllUrlProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if(predicate(exchange)) {
            //验证token各式并设置userId到Request的Hander中
            request = headerEnhanceFilter.doFilter(request);
            //获取Token
            String accessToken = extractHeaderToken(request);
            //Token到/oauth/check_token Oauth2服务中去验证
            customRemoteTokenServices.loadAuthentication(accessToken);
            LOGGER.info("success auth token and permission!");
        }

        return chain.filter(exchange.mutate().request(request).build());
    }

    public Boolean predicate(ServerWebExchange serverWebExchange) {
        URI uri = serverWebExchange.getRequest().getURI();
        String requestUri = uri.getPath();
        String authorization = serverWebExchange.getRequest().getHeaders().getFirst(OAuth2AccessToken.AUTHORIZATION);
        if (isPermitUrl(requestUri) && (StringUtils.isBlank(authorization) || (StringUtils.isNotBlank(authorization) &&
                StringUtils.countMatches(authorization, ".") != 2))) {
            return false;
        }
        if(isLogoutUrl(requestUri)) {
            return false;
        }
        return true;
    }

    private boolean isLogoutUrl(String url) {
        return url.contains("/login/logout");
    }

    private boolean isPermitUrl(String url) {
        return permitAllUrlProperties.isPermitAllUrl(url) || url.contains("/login/oauth");
    }

    protected String extractHeaderToken(ServerHttpRequest request) {
        List<String> headers = request.getHeaders().get(OAuth2AccessToken.AUTHORIZATION);
        if(!CollectionUtils.isEmpty(headers)) {
            String value = headers.get(0);
            if(value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase())) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(",");
                if(commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }

        return null;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
