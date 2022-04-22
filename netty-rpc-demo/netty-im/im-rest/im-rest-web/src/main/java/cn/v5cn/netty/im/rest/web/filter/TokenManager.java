package cn.v5cn.netty.im.rest.web.filter;

import cn.v5cn.netty.im.common.util.IdWorker;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class TokenManager {

    private static final String SESSION_KEY = "IM:TOKEN:";
    private ReactiveRedisTemplate<String, String> template;

    public TokenManager(ReactiveRedisTemplate<String, String> template) {
        this.template = template;
    }

    public Mono<String> validateToken(String token) {
        return template.opsForValue().get(SESSION_KEY + token).map(id -> {
            template.expire(SESSION_KEY + token, Duration.ofMinutes(30));
            return id;
        }).switchIfEmpty(Mono.empty());
    }

    public Mono<String> createNewToken(String userId) {
        String token = IdWorker.uuid();
        return template.opsForValue().set(SESSION_KEY + token, userId)
                .flatMap(b -> b ? template.expire(SESSION_KEY + token, Duration.ofMinutes(30)) : Mono.just(false))
                .flatMap(b -> b ? Mono.just(token) : Mono.empty());
    }

    public Mono<Boolean> expire(String token) {
        return template.delete(SESSION_KEY + token).map(l -> l > 0);
    }
}
