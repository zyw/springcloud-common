package cn.v5cn.tomcat.handler;

import cn.v5cn.tomcat.message.AuthRequest;
import cn.v5cn.tomcat.message.AuthResponse;
import cn.v5cn.tomcat.message.UserJoinNoticeRequest;
import cn.v5cn.tomcat.util.WebSocketUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.Session;

@Component
public class AuthMessageHandler implements MessageHandler<AuthRequest> {

    @Override
    public void execute(Session session, AuthRequest message) {
        AuthResponse response = null;
        // 如果未传递 accessToken
        if(StringUtils.isEmpty(message.getAccessToken())) {
            response = new AuthResponse();
            response.setCode(1);
            response.setMessage("认证 accessToken 未传入");
            WebSocketUtil.send(session, AuthRequest.TYPE, response);
            return;
        }

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
