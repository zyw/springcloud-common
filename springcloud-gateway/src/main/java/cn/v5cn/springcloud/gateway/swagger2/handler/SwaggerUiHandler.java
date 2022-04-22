package cn.v5cn.springcloud.gateway.swagger2.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

@Component
public class SwaggerUiHandler implements HandlerFunction {

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        UiConfiguration uiConfiguration = UiConfigurationBuilder.builder().validatorUrl(null).build();
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(
                        uiConfiguration));
    }

}