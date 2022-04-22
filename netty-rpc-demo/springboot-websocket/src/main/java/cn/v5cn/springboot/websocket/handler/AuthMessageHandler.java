package cn.v5cn.springboot.websocket.handler;

import cn.v5cn.springboot.websocket.message.AuthRequest;
import cn.v5cn.springboot.websocket.message.AuthResponse;
import cn.v5cn.springboot.websocket.message.UserJoinNoticeRequest;
import cn.v5cn.springboot.websocket.util.WebSocketUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketSession;

@Component
public class AuthMessageHandler implements MessageHandler<AuthRequest> {

    @Override
    public void execute(WebSocketSession session, AuthRequest message) {
        AuthResponse response = null;
        // 如果未传递 accessToken
        if(StringUtils.isEmpty(message.getAccessToken())) {
            response = new AuthResponse();
            response.setCode(1);
            response.setMessage("认证 accessToken 未传入");
            WebSocketUtil.send(session, AuthRequest.TYPE, response);
            return;
        }
        // TODO 验证accessToken是否合法

        // 添加到 WebSocketUtil 中
        // 考虑到代码简化，我们先直接使用 accessToken 作为 User
        WebSocketUtil.addSession(session,message.getAccessToken());

        response = new AuthResponse();
        response.setCode(0);
        WebSocketUtil.send(session, AuthRequest.TYPE, response);

        // 通知所有人，某个人加入了。这个是可选逻辑，仅仅是为了演示
        UserJoinNoticeRequest userJoinNoticeRequest = new UserJoinNoticeRequest();
        userJoinNoticeRequest.setNickname(message.getAccessToken());
        // 考虑到代码简化，我们先直接使用 accessToken 作为 User
        WebSocketUtil.broadcast(UserJoinNoticeRequest.TYPE, userJoinNoticeRequest);
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}
