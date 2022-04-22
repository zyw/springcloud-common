package cn.v5cn.netty.im.rest.web.filter;

import cn.v5cn.netty.im.common.exception.ImException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class HeaderFilter implements WebFilter {

    private TokenManager tokenManager;

    public HeaderFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String path = serverWebExchange.getRequest().getPath().value();

        if("/user/login".equals(path) || path.startsWith("/offline")) {
            return webFilterChain.filter(serverWebExchange);
        }
        if(!serverWebExchange.getRequest().getHeaders().containsKey("token")) {
            return Mono.error(new ImException("[rest] user is not login"));
        }

        final String token = serverWebExchange.getRequest().getHeaders().getFirst("token");

        return tokenManager.validateToken(token).flatMap(b -> b != null ? webFilterChain.filter(serverWebExchange) :
                Mono.error(new ImException("[rest] user is not login")));
    }
}
