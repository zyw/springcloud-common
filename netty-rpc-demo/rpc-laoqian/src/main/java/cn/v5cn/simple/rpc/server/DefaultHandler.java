package cn.v5cn.simple.rpc.server;

import cn.v5cn.simple.rpc.common.IMessageHandler;
import cn.v5cn.simple.rpc.common.MessageInput;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认消息处理器，找不到类型的消息统一使用默认处理器处理
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 17:50
 */
public class DefaultHandler implements IMessageHandler<MessageInput> {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, MessageInput input) {
        LOG.error("unrecognized message type {} comes", input.getType());
        ctx.close();
    }
}
