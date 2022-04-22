package cn.v5cn.netty.im.rest.web.handler;

import cn.v5cn.netty.im.common.domain.ResultWrapper;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.rest.web.service.OfflineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class OfflineHandler {

    private OfflineService offlineService;

    public OfflineHandler(OfflineService offlineService) {
        this.offlineService = offlineService;
    }

    public Mono<ServerResponse> pollOfflineMsg(ServerRequest request) {

        String id = request.pathVariable("id");

        return Mono.fromSupplier(() -> {
            try {
                return offlineService.pollOfflineMsg(id);
            } catch (JsonProcessingException e) {
                throw new ImException(e);
            }
        }).map(ResultWrapper::success)
          .flatMap(res -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(res)));
    }
}
