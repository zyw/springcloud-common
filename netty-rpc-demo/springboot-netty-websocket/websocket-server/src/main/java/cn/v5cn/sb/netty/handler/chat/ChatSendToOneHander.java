package cn.v5cn.sb.netty.handler.chat;

import cn.v5cn.sb.netty.common.codec.Invocation;
import cn.v5cn.sb.netty.common.dispacher.MessageHandler;
import cn.v5cn.sb.netty.message.chat.ChatRedirectToUserRequest;
import cn.v5cn.sb.netty.message.chat.ChatSendResponse;
import cn.v5cn.sb.netty.message.chat.ChatSendToOneRequest;
import cn.v5cn.sb.netty.server.NettyChannelManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatSendToOneHander implements MessageHandler<ChatSendToOneRequest> {

    @Autowired
    private NettyChannelManager manager;

    @Override
    public void execute(Channel channel, ChatSendToOneRequest message) {
        // <1> 这里，假装直接成功
        ChatSendResponse sendResponse = new ChatSendResponse();
        sendResponse.setMsgId(message.getMsgId());
        sendResponse.setCode(0);
        channel.writeAndFlush(new Invocation(ChatSendResponse.TYPE, sendResponse));

        // <2> 创建转发的消息，发送给指定用户
        ChatRedirectToUserRequest sendToUserRequest = new ChatRedirectToUserRequest();
        sendToUserRequest.setMsgId(message.getMsgId());
        sendToUserRequest.setFromUser(message.getFromUser());
        sendToUserRequest.setContent(message.getContent());
        manager.send(message.getToUser(), new Invocation(ChatRedirectToUserRequest.TYPE, sendToUserRequest));
    }

    @Override
    public String getType() {
        return ChatSendToOneRequest.TYPE;
    }
}
