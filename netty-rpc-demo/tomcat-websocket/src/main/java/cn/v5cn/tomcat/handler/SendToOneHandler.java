package cn.v5cn.tomcat.handler;

import cn.v5cn.tomcat.message.SendResponse;
import cn.v5cn.tomcat.message.SendToOneRequest;
import cn.v5cn.tomcat.message.SendToUserRequest;
import cn.v5cn.tomcat.util.WebSocketUtil;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

@Component
public class SendToOneHandler implements MessageHandler<SendToOneRequest> {
    @Override
    public void execute(Session session, SendToOneRequest message) {
        // 这里，假装直接成功
        SendResponse response = new SendResponse();
        response.setMsgId(message.getMsgId());
        response.setCode(0);
        WebSocketUtil.send(session, SendResponse.TYPE, response);

        // 创建转发的消息
        SendToUserRequest toUserRequest = new SendToUserRequest();
        toUserRequest.setMsgId(message.getMsgId());
        toUserRequest.setContent(message.getContent());

        //发送消息
        WebSocketUtil.send(message.getToUser(), SendToUserRequest.TYPE, toUserRequest);
    }

    @Override
    public String getType() {
        return SendToOneRequest.TYPE;
    }
}
