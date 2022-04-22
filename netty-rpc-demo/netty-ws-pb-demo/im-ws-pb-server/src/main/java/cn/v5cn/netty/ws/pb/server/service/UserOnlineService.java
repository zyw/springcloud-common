package cn.v5cn.netty.ws.pb.server.service;

import cn.v5cn.netty.ws.pb.core.constant.Constants;
import cn.v5cn.netty.ws.pb.core.constant.MsgVersion;
import cn.v5cn.netty.ws.pb.core.entity.AckMsg;
import cn.v5cn.netty.ws.pb.core.entity.ChatMsg;
import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;
import cn.v5cn.netty.ws.pb.core.util.IdWorker;
import cn.v5cn.netty.ws.pb.server.domain.ClientConn;
import cn.v5cn.netty.ws.pb.server.domain.ClientConnContext;
import cn.v5cn.netty.ws.pb.server.status.UserStatusService;
import cn.v5cn.netty.ws.pb.server.status.impl.MemoryUserStatusServiceImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author zyw
 */
@Singleton
public class UserOnlineService {
    private final ClientConnContext clientConnContext;
    private final ServerToClientService serverToClientService;
    private final OfflineService offlineService;
    private final UserStatusService userStatusService;

    @Inject
    public UserOnlineService(
            OfflineService offlineService,
            ClientConnContext clientConnContext,
            ServerToClientService serverToClientService
            ) {
        this.clientConnContext = clientConnContext;
        this.offlineService = offlineService;
        this.serverToClientService = serverToClientService;
        this.userStatusService = new MemoryUserStatusServiceImpl();
    }

    public ClientConn userOnline(String userId, ChannelHandlerContext ctx) {
        // 获得全部离线消息并发送
        List<Message> messages = offlineService.pollOfflineMsg(userId);
        messages.forEach(msg -> {
            try {
                ChatMsg chatMsg = (ChatMsg) msg;
                serverToClientService.doChatToClientAndFlush(chatMsg);
            } catch (ClassCastException ex) {
                AckMsg ackMsg = (AckMsg) msg;
                serverToClientService.doSendAckToClientAndFlush(ackMsg);
            }
        });

        // 保存连接
        ClientConn conn = new ClientConn(ctx);
        conn.setUserId(userId);
        clientConnContext.addConn(conn);

        // 用户上线
        final String oldConnectorId = userStatusService.online(userId, Constants.CONNECTOR_ID);
        if(oldConnectorId != null) {
            // can't online twice
            sendErrorToClient("already online", ctx);
        }
        return conn;
    }

    public void userOffline(ChannelHandlerContext ctx) {
        ClientConn conn = clientConnContext.getConn(ctx);
        if (conn == null ){
            return;
        }
        userStatusService.offline(conn.getUserId());
        // remove the connection
        clientConnContext.removeConn(ctx);
    }

    private void sendErrorToClient(String errorMsg, ChannelHandlerContext ctx) {
        InternalMsg errorAck = InternalMsg.newBuilder()
                .setId(IdWorker.snowGenId())
                .setVersion(MsgVersion.V1.getVersion())
                .setFrom(InternalMsg.Module.CONNECTOR)
                .setDest(InternalMsg.Module.CLIENT)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(InternalMsg.MsgType.ERROR)
                .setMsgBody(errorMsg)
                .build();

        ctx.writeAndFlush(errorAck);
    }
}
