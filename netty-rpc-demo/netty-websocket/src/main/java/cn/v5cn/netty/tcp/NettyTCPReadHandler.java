package cn.v5cn.netty.tcp;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

public class NettyTCPReadHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyTCPReadHandler.class);

    private NettyTCPIMS ims;

    NettyTCPReadHandler(NettyTCPIMS ims) {
        this.ims = ims;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelActive() ctx = " + ctx);
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.debug(String.format("客户端【%1$s:%2$d】已连接)", address.getHostName(), address.getPort()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("channelInactive() ctx = " + ctx);
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.debug(String.format("客户端【%1$s:%2$d】已断开连接)", address.getHostName(), address.getPort()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught() ctx = " + ctx + "\tcause = " + cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("channelRead() ctx = " + ctx + "\tmsg = " + msg);
    }
}
