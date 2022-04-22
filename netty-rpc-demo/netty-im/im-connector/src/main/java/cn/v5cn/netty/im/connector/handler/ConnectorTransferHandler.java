package cn.v5cn.netty.im.connector.handler;

import cn.v5cn.netty.im.common.domain.ack.ServerAckWindow;
import cn.v5cn.netty.im.common.domain.conn.Conn;
import cn.v5cn.netty.im.common.domain.constant.MsgVersion;
import cn.v5cn.netty.im.common.parse.AbstractMsgParser;
import cn.v5cn.netty.im.common.parse.InternalParser;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.connector.service.ConnectorToClientService;
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
import java.util.ArrayList;
import java.util.List;

import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkDest;
import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkFrom;


/**
 * @author yrw
 */
public class ConnectorTransferHandler extends SimpleChannelInboundHandler<Message> {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorTransferHandler.class);

    public static final String CONNECTOR_ID = IdWorker.uuid();
    private static final List<ChannelHandlerContext> CTX_LIST = new ArrayList<>();

    private ServerAckWindow serverAckWindow;
    private final FromTransferParser fromTransferParser;
    private final ConnectorToClientService connectorToClientService;

    @Inject
    public ConnectorTransferHandler(ConnectorToClientService connectorToClientService) {
        this.fromTransferParser = new FromTransferParser();
        this.connectorToClientService = connectorToClientService;
    }

    public static ChannelHandlerContext getOneOfTransferCtx(long time) {
        if(CTX_LIST.size() == 0) {
            logger.warn("connector is not connected to a transfer!");
        }
        return CTX_LIST.get((int)(time % CTX_LIST.size()));
    }

    public static List<ChannelHandlerContext> getCtxList() {
        if(CTX_LIST.size() == 0) {
            logger.warn("connector is not connected to a transfer!");
        }
        return CTX_LIST;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        logger.debug("[connector] get msg: {}", message.toString());

        checkFrom(message, Internal.InternalMsg.Module.TRANSFER);
        checkDest(message, Internal.InternalMsg.Module.CONNECTOR);

        fromTransferParser.parse(message, ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("[ConnectorTransfer] connect to transfer");

        putConnectionId(ctx);
        serverAckWindow = new ServerAckWindow(IdWorker.uuid(), 10, Duration.ofSeconds(5));
        greetToTransfer(ctx);

        CTX_LIST.add(ctx);
    }

    private void greetToTransfer(ChannelHandlerContext ctx) {
        Internal.InternalMsg greet = Internal.InternalMsg.newBuilder()
                .setId(IdWorker.snowGenId())
                .setVersion(MsgVersion.V1.getVersion())
                .setMsgType(Internal.InternalMsg.MsgType.GREET)
                .setMsgBody(CONNECTOR_ID)
                .setFrom(Internal.InternalMsg.Module.CONNECTOR)
                .setDest(Internal.InternalMsg.Module.TRANSFER)
                .setCreateTime(System.currentTimeMillis())
                .build();

        serverAckWindow.offer(greet.getId(), greet, ctx::writeAndFlush)
                .thenAccept(m -> logger.info("[connector] connect to transfer successfully"))
                .exceptionally(e -> {
                    logger.error("[connector] waiting for transfer's response failed", e);
                    return null;
                });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //todo: reconnect
    }

    class FromTransferParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {
            InternalParser parser = new InternalParser(3);
            parser.register(Internal.InternalMsg.MsgType.ACK, (m, ctx) -> serverAckWindow.ack(m));

            register(Chat.ChatMsg.class, (m, ctx) -> connectorToClientService.doChatToClientAndFlush(m));
            register(Ack.AckMsg.class, (m, ctx) -> connectorToClientService.doSendAckToClientAndFlush(m));
            register(Internal.InternalMsg.class, parser.generateFun());
        }
    }

    public void putConnectionId(ChannelHandlerContext ctx) {
        ctx.channel().attr(Conn.NET_ID).set(IdWorker.uuid());
    }
}
