package cn.v5cn.netty.ws.pb.server.handler;

import cn.v5cn.netty.ws.pb.core.constant.MsgVersion;
import cn.v5cn.netty.ws.pb.core.entity.AckMsg;
import cn.v5cn.netty.ws.pb.core.entity.ChatMsg;
import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;
import cn.v5cn.netty.ws.pb.core.exception.ImLogicException;
import cn.v5cn.netty.ws.pb.core.handler.MessageHandler;
import cn.v5cn.netty.ws.pb.core.handler.ServerAckHandler;
import cn.v5cn.netty.ws.pb.core.parser.AbstractMsgParser;
import cn.v5cn.netty.ws.pb.core.parser.ChatEnumParser;
import cn.v5cn.netty.ws.pb.core.parser.InternalEnumParser;
import cn.v5cn.netty.ws.pb.core.util.IdWorker;
import cn.v5cn.netty.ws.pb.server.domain.ClientConn;
import cn.v5cn.netty.ws.pb.server.domain.ClientConnContext;
import cn.v5cn.netty.ws.pb.server.service.ServerToClientService;
import cn.v5cn.netty.ws.pb.server.service.UserOnlineService;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Consumer;

import static cn.v5cn.netty.ws.pb.core.parser.AbstractMsgParser.checkDest;
import static cn.v5cn.netty.ws.pb.core.parser.AbstractMsgParser.checkFrom;

/**
 * @author zyw
 */
public class WsPbServerHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WsPbServerHandler.class);

    private final ServerToClientService serverToClientService;
    private final UserOnlineService userOnlineService;
    private final ClientConnContext clientConnContext;
    private final FromClientParser fromClientParser;


    private MessageHandler messageHandler;
    private ServerAckHandler serverAckHandler;

    public WsPbServerHandler(ServerToClientService serverToClientService,
                             UserOnlineService userOnlineService,
                             ClientConnContext clientConnContext) {
        fromClientParser = new FromClientParser();
        this.serverToClientService = serverToClientService;
        this.userOnlineService = userOnlineService;
        this.clientConnContext = clientConnContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        LOGGER.debug("[connector] get msg: {}", msg.toString());

        checkFrom(msg, InternalMsg.Module.CLIENT);
        checkDest(msg, InternalMsg.Module.CONNECTOR);

        fromClientParser.parse(msg, ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOnlineService.userOffline(ctx);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("[IM ConnectorClientHandler] has error: ", cause);
        clientConnContext.removeConn(ctx);
    }

    public InternalMsg getAck(Long id) {
        return InternalMsg.newBuilder()
                .setVersion(MsgVersion.V1.getVersion())
                .setId(IdWorker.snowGenId())
                .setFrom(InternalMsg.Module.CONNECTOR)
                .setDest(InternalMsg.Module.CLIENT)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(InternalMsg.MsgType.ACK)
                .setMsgBody(id + "")
                .build();
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    class FromClientParser extends AbstractMsgParser {

        @Override
        public void registerParser() {
            InternalEnumParser parser = new InternalEnumParser(3);
            parser.register(InternalMsg.MsgType.GREET,(m, ctx) -> {
                final ClientConn conn = userOnlineService.userOnline(m.getMsgBody(), ctx);
                messageHandler = new MessageHandler(5);
                serverAckHandler = new ServerAckHandler(conn.getNetId(), 10, Duration.ofSeconds(5));
                ctx.writeAndFlush(getAck(m.getId()));
            });
            parser.register(InternalMsg.MsgType.ACK, (m, ctx) -> serverAckHandler.ack(m));

            ChatEnumParser chatParser = new ChatEnumParser(3);
            chatParser.register(ChatMsg.DestType.SINGLE, (m, ctx) -> sendChat(m.getId(),m,ctx, ignore -> serverToClientService.doChatToClientAndFlush(m)));
            chatParser.register(ChatMsg.DestType.GROUP, (m, ctx) -> sendChat(m.getId(), m,ctx, ignore -> serverToClientService.doChatGroupToClientAndFlush(m)));
//            register(ChatMsg.class, (m, ctx) -> sendChat(m.getId(),m,ctx, ignore -> serverToClientService.doChatToClientAndFlush(m)));

            register(ChatMsg.class, chatParser.generateFun());

            register(AckMsg.class, (m, ctx) -> sendAck(m.getId(), m, ctx, ignore -> serverToClientService.doSendAckToClientAndFlush(m)));

            register(InternalMsg.class, parser.generateFun());
        }

        private void sendAck(Long id, AckMsg msg, ChannelHandlerContext ctx, Consumer<Message> consumer) {
            AckMsg copy = AckMsg.newBuilder().mergeFrom(msg).build();
            send(id, copy, ctx, consumer);
        }

        private void sendChat(Long id, ChatMsg msg, ChannelHandlerContext ctx, Consumer<Message> consumer) {
            ChatMsg copy = ChatMsg.newBuilder().mergeFrom(msg).build();
            send(id, copy, ctx, consumer);
        }

        private void send(Long id, Message msg, ChannelHandlerContext ctx, Consumer<Message> consumer) {

            if(messageHandler == null) {
                throw new ImLogicException("client not greet yet");
            }

            messageHandler.send(
                    id,
                    ctx,
                    InternalMsg.Module.CONNECTOR,
                    InternalMsg.Module.CLIENT,
                    msg,
                    consumer
            );
        }
    }
}