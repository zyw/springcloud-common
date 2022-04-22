package cn.v5cn.liyue.rpc.netty.transport.netty;

import cn.v5cn.liyue.rpc.netty.transport.RequestHandler;
import cn.v5cn.liyue.rpc.netty.transport.RequestHandlerRegistry;
import cn.v5cn.liyue.rpc.netty.transport.command.Command;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求处理Handler
 * @author LiYue
 * Date: 2019/9/20
 */
@ChannelHandler.Sharable
public class RequestInvocation extends SimpleChannelInboundHandler<Command> {

    private static final Logger logger = LoggerFactory.getLogger(RequestInvocation.class);

    private final RequestHandlerRegistry requestHandlerRegistry;

    RequestInvocation(RequestHandlerRegistry requestHandlerRegistry) {
        this.requestHandlerRegistry = requestHandlerRegistry;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {
        RequestHandler requestHandler = requestHandlerRegistry.get(command.getHeader().getRequestId());
        if(null != requestHandler) {
            Command response = requestHandler.handle(command);
            if(null != response) {
                channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener)channelFuture -> {
                    if(!channelFuture.isSuccess()){
                        logger.warn("Write response failed!", channelFuture.cause());
                        channelHandlerContext.channel().close();
                    }
                });
            } else {
                logger.warn("Response is null!");
            }
        } else {
            throw new Exception(String.format("No handler for request with type: %d!", command.getHeader().getType()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Exception: ",cause);
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if(channel.isActive()) ctx.close();
    }
}
