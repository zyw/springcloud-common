package cn.v5cn.sb.netty.handler.chat;

import cn.v5cn.sb.netty.common.dispacher.MessageHandler;
import cn.v5cn.sb.netty.message.chat.ChatRedirectToUserRequest;
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
public class ChatRedirectToUserRequestHandler implements MessageHandler<ChatRedirectToUserRequest> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, ChatRedirectToUserRequest message) {
        logger.info("[execute][收到消息：{}]", message);
    }

    @Override
    public String getType() {
        return ChatRedirectToUserRequest.TYPE;
    }
}
