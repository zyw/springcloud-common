package cn.v5cn.netty.im.rest.web.handler;

import cn.v5cn.netty.im.common.domain.ResultWrapper;
import cn.v5cn.netty.im.common.domain.UserInfo;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.rest.spi.UserSpi;
import cn.v5cn.netty.im.rest.spi.domain.UserBase;
import cn.v5cn.netty.im.rest.web.filter.TokenManager;
import cn.v5cn.netty.im.rest.web.service.RelationService;
import cn.v5cn.netty.im.rest.web.spi.SpiFactory;
import cn.v5cn.netty.im.rest.web.vo.UserReq;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class UserHandler {

    private UserSpi<? extends UserBase> userSpi;
    private RelationService relationService;
    private TokenManager tokenManager;

    public UserHandler(SpiFactory spiFactory, RelationService relationService, TokenManager tokenManager) {
        this.userSpi = spiFactory.getUserSpi();
        this.relationService = relationService;
        this.tokenManager = tokenManager;
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return ValidHandler.requireValidBody(req ->
                req.flatMap(login -> {
                    final UserBase user = userSpi.getUser(login.getUsername(), login.getPwd());
                    return user != null ? Mono.just(user) : Mono.empty();
                })
                .flatMap(u -> tokenManager.createNewToken(u.getId())
                        .map(t -> {
                            UserInfo userInfo = new UserInfo();
                            userInfo.setId(u.getId());
                            userInfo.setUsername(u.getUsername());
                            userInfo.setToken(t);
                            return userInfo;
                        }))
                .flatMap(u -> Flux.fromIterable(relationService.friends(u.getId()))
                .collectList()
                .map(list -> {
                    u.setRelations(list);
                    return u;
                }))
                .map(ResultWrapper::success)
                .flatMap(info -> ok().body(fromObject(info)))
                .switchIfEmpty(Mono.error(new ImException("[rest] authentication failed")))
                , request, UserReq.class);
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        final String token = request.headers().header("token").get(0);

        return tokenManager.expire(token).map(ResultWrapper::wrapBol)
                .flatMap(r -> ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(r)));
    }
}
