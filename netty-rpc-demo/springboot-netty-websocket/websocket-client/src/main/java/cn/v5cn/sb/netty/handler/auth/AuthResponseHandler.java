package cn.v5cn.sb.netty.handler.auth;

import cn.v5cn.sb.netty.common.dispacher.MessageHandler;
import cn.v5cn.sb.netty.message.auth.AuthResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:29 下午
 */
@Component
public class AuthResponseHandler implements MessageHandler<AuthResponse> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, AuthResponse message) {
        logger.info("[execute][认证结果：{}]", message);
    }

    @Override
    public String getType() {
        return AuthResponse.TYPE;
    }
}
