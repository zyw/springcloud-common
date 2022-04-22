package cn.v5cn.netty.im.rest.web.handler;

import cn.v5cn.netty.im.common.exception.ImException;
import com.google.common.collect.Iterables;
import net.sf.jsqlparser.schema.Server;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.function.Function;

public class ValidHandler {

    private static Validator validator;

    public static <BODY> Mono<ServerResponse> requireValidBody(
            Function<Mono<BODY>, Mono<ServerResponse>> block,
            ServerRequest request, Class<BODY> bodyClass) {

        return request
                .bodyToMono(bodyClass)
                .flatMap(body -> {
                   Set<ConstraintViolation<BODY>> msg = validator.validate(body);
                   if(msg.isEmpty()) {
                       return block.apply(Mono.just(body));
                   } else {
                       ConstraintViolation v = Iterables.get(msg, 0);
                       throw new ImException(v.getPropertyPath() + " " + v.getMessage());
                   }
                });
    }

    public static void setValidator(Validator validator) {
        ValidHandler.validator = validator;
    }

}
