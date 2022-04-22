package cn.v5cn.springboot.websocket.handler;

import cn.v5cn.springboot.websocket.message.SendResponse;
import cn.v5cn.springboot.websocket.message.SendToAllRequest;
import cn.v5cn.springboot.websocket.message.SendToUserRequest;
import cn.v5cn.springboot.websocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SendToAllHandler implements MessageHandler<SendToAllRequest> {
    @Override
    public void execute(WebSocketSession session, SendToAllRequest message) {
        // 这里，假装直接成功
        SendResponse response = new SendResponse();
        response.setMsgId(message.getMsgId());
        response.setCode(0);
        WebSocketUtil.send(session, SendResponse.TYPE, response);

        // 创建转发的消息
        SendToUserRequest toUserRequest = new SendToUserRequest();
        toUserRequest.setMsgId(message.getMsgId());
        toUserRequest.setContent(message.getContent());

        // 广播发送
        WebSocketUtil.broadcast(SendToUserRequest.TYPE, toUserRequest);
    }

    @Override
    public String getType() {
        return SendToAllRequest.TYPE;
    }
}
