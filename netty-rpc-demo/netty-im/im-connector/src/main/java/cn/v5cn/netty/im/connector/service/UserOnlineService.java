package cn.v5cn.netty.im.connector.service;

import cn.v5cn.netty.im.common.domain.constant.MsgVersion;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.connector.domain.ClientConn;
import cn.v5cn.netty.im.connector.domain.ClientConnContext;
import cn.v5cn.netty.im.connector.handler.ConnectorTransferHandler;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import cn.v5cn.netty.im.user_status.factory.UserStatusServiceFactory;
import cn.v5cn.netty.im.user_status.service.UserStatusService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Properties;

import static cn.v5cn.netty.im.connector.start.ConnectorStarter.CONNECTOR_CONFIG;

/**
 * @author yrw
 */
@Singleton
public class UserOnlineService {

    private final ClientConnContext clientConnContext;
    private final ConnectorToClientService connectorToClientService;
    private final OfflineService offlineService;
    private final UserStatusService userStatusService;

    @Inject
    public UserOnlineService(OfflineService offlineService,
                             ClientConnContext clientConnContext,
                             ConnectorToClientService connectorToClientService,
                             UserStatusServiceFactory userStatusServiceFactory) {

        this.clientConnContext = clientConnContext;
        this.offlineService = offlineService;
        this.connectorToClientService = connectorToClientService;

        Properties properties = new Properties();
        properties.put("host", CONNECTOR_CONFIG.getRedisHost());
        properties.put("port", CONNECTOR_CONFIG.getRedisPort());
        properties.put("password", CONNECTOR_CONFIG.getRedisPassword());
        this.userStatusService = userStatusServiceFactory.createService(properties);
    }

    public ClientConn userOnline(String userId, ChannelHandlerContext ctx){
        // get all offline msg and send
        // 获得全部离线消息并发送
        final List<Message> messages = offlineService.pollOfflineMsg(userId);
        messages.forEach(msg -> {
            try {
                Chat.ChatMsg chatMsg = (Chat.ChatMsg)msg;
                connectorToClientService.doChatToClientAndFlush(chatMsg);
            } catch (ClassCastException ex) {
                Ack.AckMsg ackMsg = (Ack.AckMsg)msg;
                connectorToClientService.doSendAckToClientAndFlush(ackMsg);
            }
        });

        // save connection
        // 保存连接
        ClientConn conn = new ClientConn(ctx);
        conn.setUserId(userId);
        clientConnContext.addConn(conn);

        // user is online
        // 用户上线
        final String oldConnectorId  = userStatusService.online(userId, ConnectorTransferHandler.CONNECTOR_ID);
        if(oldConnectorId != null) {
            //can't online twice
            sendErrorToClient("already online", ctx);
        }
        return conn;
    }

    private void sendErrorToClient(String errorMsg, ChannelHandlerContext ctx) {
        Internal.InternalMsg errorAck = Internal.InternalMsg.newBuilder()
                .setId(IdWorker.snowGenId())
                .setVersion(MsgVersion.V1.getVersion())
                .setFrom(Internal.InternalMsg.Module.CONNECTOR)
                .setDest(Internal.InternalMsg.Module.CLIENT)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(Internal.InternalMsg.MsgType.ERROR)
                .setMsgBody(errorMsg)
                .build();

        ctx.writeAndFlush(errorAck);
    }

    public void userOffline(ChannelHandlerContext ctx) {
        final ClientConn conn = clientConnContext.getConn(ctx);
        if(conn == null) {
            return;
        }
        userStatusService.offline(conn.getUserId());
        //remove the connection
        clientConnContext.removeConn(ctx);
    }
}
