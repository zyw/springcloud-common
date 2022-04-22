package cn.v5cn.netty.im.transfer.handler;

import cn.v5cn.netty.im.common.parse.AbstractMsgParser;
import cn.v5cn.netty.im.common.parse.InternalParser;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import cn.v5cn.netty.im.transfer.domain.ConnectorConnContext;
import cn.v5cn.netty.im.transfer.service.TransferService;
import com.google.inject.Inject;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkDest;
import static cn.v5cn.netty.im.common.parse.AbstractMsgParser.checkFrom;

/**
 * @author yrw
 */
public class TransferConnectorHandler extends SimpleChannelInboundHandler<Message> {
    private Logger logger = LoggerFactory.getLogger(TransferConnectorHandler.class);

    private TransferService transferService;
    private ConnectorConnContext connContext;
    private FromConnectorParser fromConnectorParser;

    @Inject
    public TransferConnectorHandler(TransferService transferService, ConnectorConnContext connContext) {
        this.fromConnectorParser = new FromConnectorParser();
        this.transferService = transferService;
        this.connContext = connContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        logger.debug("[transfer] get msg: {}", msg.toString());

        checkFrom(msg, Internal.InternalMsg.Module.CONNECTOR);
        checkDest(msg, Internal.InternalMsg.Module.TRANSFER);

        fromConnectorParser.parse(msg, ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connContext.removeConn(ctx);
    }

    class FromConnectorParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {
            InternalParser parser = new InternalParser(3);
            parser.register(Internal.InternalMsg.MsgType.GREET,(m, ctx) -> transferService.doGreet(m, ctx));

            register(Chat.ChatMsg.class, (m, ctx) -> transferService.doChat(m));
            register(Ack.AckMsg.class, (m, ctx) -> transferService.doSendAck(m));
            register(Internal.InternalMsg.class, parser.generateFun());
        }
    }
}
