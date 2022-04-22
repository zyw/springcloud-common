package cn.v5cn.netty.im.rest.web.router;

import cn.v5cn.netty.im.rest.web.handler.OfflineHandler;
import cn.v5cn.netty.im.rest.web.handler.RelationHandler;
import cn.v5cn.netty.im.rest.web.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RestRouter {

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return RouterFunctions
                .route(POST("/user/login").and(contentType(MediaType.APPLICATION_JSON)).and(accept(MediaType.APPLICATION_JSON)),
                        userHandler::login)
                .andRoute(GET("/user/logout").and(accept(MediaType.APPLICATION_JSON)),
                        userHandler::logout);
    }

    @Bean
    public RouterFunction<ServerResponse> relationRoutes(RelationHandler relationHandler) {
        return RouterFunctions
                .route(GET("/relation/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        relationHandler::listFriends)
                .andRoute(GET("/relation").and(accept(MediaType.APPLICATION_JSON)),
                        relationHandler::getRelation)
                .andRoute(POST("/relation").and(contentType(MediaType.APPLICATION_JSON)),
                        relationHandler::saveRelation)
                .andRoute(DELETE("relation/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        relationHandler::deleteRelation);
    }

    @Bean
    public RouterFunction<ServerResponse> offlineRoutes(OfflineHandler offlineHandler) {
        //only for connector
        return RouterFunctions.route(GET("/offline/poll/{id}")
                .and(accept(MediaType.APPLICATION_JSON)), offlineHandler::pollOfflineMsg);
    }
}
