package cn.v5cn.sb.netty.handler.chat;

import cn.v5cn.sb.netty.common.dispacher.MessageHandler;
import cn.v5cn.sb.netty.message.chat.ChatSendResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:30 下午
 */
@Component
public class ChatSendResponseHandler implements MessageHandler<ChatSendResponse> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, ChatSendResponse message) {
        logger.info("[execute][发送结果：{}]", message);
    }

    @Override
    public String getType() {
        return ChatSendResponse.TYPE;
    }
}
