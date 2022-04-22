package cn.v5cn.netty.im.client.handler;

import cn.v5cn.netty.im.client.api.ClientMsgListener;
import cn.v5cn.netty.im.common.domain.ack.ClientAckWindow;
import cn.v5cn.netty.im.common.domain.ack.ServerAckWindow;
import cn.v5cn.netty.im.common.parse.AbstractMsgParser;
import cn.v5cn.netty.im.common.parse.AckParser;
import cn.v5cn.netty.im.common.parse.InternalParser;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.Duration;
import java.util.function.Consumer;

import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkDest;
import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkFrom;

/**
 * @author yrw
 */
public class ClientConnectorHandler extends SimpleChannelInboundHandler<Message> {

    private Logger logger = LoggerFactory.getLogger(ClientConnectorHandler.class);

    private ClientMsgListener clientMsgListener;
    private FromConnectorParser fromConnectorParser;
    private ChannelHandlerContext ctx;

    private ServerAckWindow serverAckWindow;
    private ClientAckWindow clientAckWindow;

    public ClientConnectorHandler(ClientMsgListener clientMsgListener) {
        assert clientMsgListener != null;

        this.clientMsgListener = clientMsgListener;
        this.fromConnectorParser = new FromConnectorParser();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        serverAckWindow = new ServerAckWindow(IdWorker.uuid(), 10, Duration.ofSeconds(5));
        clientAckWindow = new ClientAckWindow(5);
        clientMsgListener.online();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        logger.debug("[client] get msg: {}", message.toString());

        checkFrom(message, Internal.InternalMsg.Module.CONNECTOR);
        checkDest(message, Internal.InternalMsg.Module.CLIENT);

        fromConnectorParser.parse(message, ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("[client] disconnect to connector");
        clientMsgListener.offline();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("[client] has error: ", cause);
        clientMsgListener.hasException(ctx, cause);
    }

    public void writeAndFlush(Serializable connectionId, Long msgId, Message message) {
        ServerAckWindow.offer(connectionId, msgId, message, m -> ctx.writeAndFlush(m))
                .thenAccept(m -> clientMsgListener.hasSent(msgId))
                .exceptionally(e -> {
                    logger.error("[client] send to connector failed", e);
                    return null;
                });
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public ServerAckWindow getServerAckWindow() {
        return serverAckWindow;
    }

    /**
     * do not use, for test
     *
     * @param serverAckWindow
     */
    public void setServerAckWindow(ServerAckWindow serverAckWindow) {
        this.serverAckWindow = serverAckWindow;
    }

    /**
     * do not use, for test
     *
     * @param clientAckWindow
     */
    public void setClientAckWindow(ClientAckWindow clientAckWindow) {
        this.clientAckWindow = clientAckWindow;
    }

    /**
     * do not use, for test
     *
     * @param ctx
     */
    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    class FromConnectorParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {
            InternalParser internalParser = new InternalParser(3);
            internalParser.register(Internal.InternalMsg.MsgType.ACK, (m, ctx) -> serverAckWindow.ack(m));
            internalParser.register(Internal.InternalMsg.MsgType.ERROR, (m, ctx) ->
                    logger.error("[client] get error from connector {}", m.getMsgBody()));

            AckParser ackParser = new AckParser(2);
            ackParser.register(Ack.AckMsg.MsgType.DELIVERED, (m, ctx) ->
                    offerAck(m.getId(), m, ignore -> clientMsgListener.hasDelivered(m.getAckMsgId())));

            ackParser.register(Ack.AckMsg.MsgType.READ, (m, ctx) ->
                    offerAck(m.getId(), m, ignore -> clientMsgListener.hasRead(m.getAckMsgId())));

            register(Chat.ChatMsg.class, (m, ctx) ->
                    offerChat(m.getId(), m, ignore -> clientMsgListener.read(m)));

            register(Ack.AckMsg.class, ackParser.generateFun());
            register(Internal.InternalMsg.class, internalParser.generateFun());
        }

        private void offerChat(Long id, Chat.ChatMsg m, Consumer<Message> consumer) {
            final Chat.ChatMsg copy = Chat.ChatMsg.newBuilder().mergeFrom(m).build();
            offer(id, copy, consumer);
        }

        private void offerAck(Long id, Ack.AckMsg m, Consumer<Message> consumer) {
            final Ack.AckMsg ackMsg = Ack.AckMsg.newBuilder().mergeFrom(m).build();
            offer(id, ackMsg, consumer);
        }

        private void offer(Long id, Message m, Consumer<Message> consumer) {
            clientAckWindow.offer(id,
                    Internal.InternalMsg.Module.CLIENT,
                    Internal.InternalMsg.Module.CONNECTOR,
                    ctx, m, consumer);
        }
    }
}
