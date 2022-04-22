package cn.v5cn.sb.netty.handler.auth;

import cn.v5cn.sb.netty.common.codec.Invocation;
import cn.v5cn.sb.netty.common.dispacher.MessageHandler;
import cn.v5cn.sb.netty.message.auth.AuthRequest;
import cn.v5cn.sb.netty.message.auth.AuthResponse;
import cn.v5cn.sb.netty.server.NettyChannelManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, AuthRequest message) {
//        String serverType = channel.attr(MessageDispatcher.CHANNEL_ATTR_KEY_SERVER_TYPE).get();
        // <1> 如果未传递 accessToken
        if (StringUtils.isEmpty(message.getAccessToken())) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setCode(1);
            authResponse.setMessage("认证 accessToken 未传入");
            /*if(Enum.valueOf(ServerTypeEnums.class,serverType) == ServerTypeEnums.TCP) {
                channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            } else {
                TextWebSocketFrame frame = new TextWebSocketFrame(JSON.toJSONString(new Invocation(AuthResponse.TYPE, authResponse)));
                channel.writeAndFlush(frame);
            }*/
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            return;
        }

        // <2> ... 此处应有一段

        // <3> 将用户和 Channel 绑定
        // 考虑到代码简化，我们先直接使用 accessToken 作为 User
        nettyChannelManager.addUser(channel, message.getAccessToken());
        // <4> 响应认证成功
        AuthResponse authResponse = new AuthResponse();
        authResponse.setCode(0);
        channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
        /*if(Enum.valueOf(ServerTypeEnums.class,serverType) == ServerTypeEnums.TCP) {
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            return;
        }
        TextWebSocketFrame frame = new TextWebSocketFrame(JSON.toJSONString(new Invocation(AuthResponse.TYPE, authResponse)));
        channel.writeAndFlush(frame);*/
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}
