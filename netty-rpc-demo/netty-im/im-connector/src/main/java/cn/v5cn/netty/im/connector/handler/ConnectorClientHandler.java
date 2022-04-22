package cn.v5cn.netty.im.connector.handler;

import cn.v5cn.netty.im.common.domain.ack.ClientAckWindow;
import cn.v5cn.netty.im.common.domain.ack.ServerAckWindow;
import cn.v5cn.netty.im.common.domain.constant.MsgVersion;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.common.parse.AbstractMsgParser;
import cn.v5cn.netty.im.common.parse.InternalParser;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.connector.domain.ClientConn;
import cn.v5cn.netty.im.connector.domain.ClientConnContext;
import cn.v5cn.netty.im.connector.service.ConnectorToClientService;
import cn.v5cn.netty.im.connector.service.UserOnlineService;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import com.google.inject.Inject;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Consumer;

import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkDest;
import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkFrom;

/**
 * 处理客户端的消息
 * Date: 2019-02-09
 * Time: 23:26
 *
 * @author yrw
 */
public class ConnectorClientHandler extends SimpleChannelInboundHandler<Message> {
    private final Logger logger = LoggerFactory.getLogger(ConnectorClientHandler.class);

    private final ConnectorToClientService connectorToClientService;
    private final UserOnlineService userOnlineService;
    private final ClientConnContext clientConnContext;
    private final FromClientParser fromClientParser;

    private ServerAckWindow serverAckWindow;
    private ClientAckWindow clientAckWindow;

    @Inject
    public ConnectorClientHandler(ConnectorToClientService connectorToClientService,
                                  UserOnlineService userOnlineService,
                                  ClientConnContext clientConnContext) {

        this.fromClientParser = new FromClientParser();
        this.connectorToClientService = connectorToClientService;
        this.userOnlineService = userOnlineService;
        this.clientConnContext = clientConnContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        logger.debug("[connector] get msg: {}", msg.toString());

        checkFrom(msg, Internal.InternalMsg.Module.CLIENT);
        checkDest(msg, Internal.InternalMsg.Module.CONNECTOR);

        fromClientParser.parse(msg, ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOnlineService.userOffline(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("[IM ConnectorClientHandler] has error: ", cause);
        clientConnContext.removeConn(ctx);
    }

    public Internal.InternalMsg getAck(Long id) {
        return Internal.InternalMsg.newBuilder()
                .setVersion(MsgVersion.V1.getVersion())
                .setId(IdWorker.snowGenId())
                .setFrom(Internal.InternalMsg.Module.CONNECTOR)
                .setDest(Internal.InternalMsg.Module.CLIENT)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(Internal.InternalMsg.MsgType.ACK)
                .setMsgBody(id + "")
                .build();
    }

    public void setClientAckWindow(ClientAckWindow clientAckWindow) {
        this.clientAckWindow = clientAckWindow;
    }

    class FromClientParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {
            InternalParser parser = new InternalParser(3);

            //do not use clientAckWindow, buz don't know netId yet
            parser.register(Internal.InternalMsg.MsgType.GREET, (m, ctx) -> {
                // 用户登录发送离线消息
                final ClientConn conn = userOnlineService.userOnline(m.getMsgBody(), ctx);
                serverAckWindow = new ServerAckWindow(conn.getNetId(), 10, Duration.ofSeconds(5));
                clientAckWindow = new ClientAckWindow(5);
                ctx.writeAndFlush(getAck(m.getId()));
            });

            parser.register(Internal.InternalMsg.MsgType.ACK, (m, ctx) -> serverAckWindow.ack(m));

            // now we know netId
            register(Chat.ChatMsg.class,
                    (m, ctx) -> offerChat(
                            m.getId(),
                            m,
                            ctx,
                            ignore -> connectorToClientService.doChatToClientOrTransferAndFlush(m)
                    ));

            register(Ack.AckMsg.class,
                    (m, ctx) -> offerAck(
                            m.getId(),
                            m,
                            ctx,
                            ignore -> connectorToClientService.doSendAckToClientOrTransferAndFlush(m)
                    ));

            register(Internal.InternalMsg.class, parser.generateFun());
        }

        private void offerChat(Long id, Chat.ChatMsg m, ChannelHandlerContext ctx, Consumer<Message> consumer) {
            final Chat.ChatMsg copy = Chat.ChatMsg.newBuilder().mergeFrom(m).build();
            offer(id, copy, ctx, consumer);
        }

        private void offerAck(Long id, Ack.AckMsg m, ChannelHandlerContext ctx, Consumer<Message> consumer) {
            final Ack.AckMsg copy = Ack.AckMsg.newBuilder().mergeFrom(m).build();
            offer(id, copy, ctx, consumer);
        }

        private void offer(Long id, Message m, ChannelHandlerContext ctx, Consumer<Message> consumer) {
            if(clientAckWindow == null) {
                throw new ImException("client not greet yet");
            }
            clientAckWindow.offer(
                    id,
                    Internal.InternalMsg.Module.CONNECTOR,
                    Internal.InternalMsg.Module.CLIENT,
                    ctx,
                    m,
                    consumer
            );
        }
    }
}
