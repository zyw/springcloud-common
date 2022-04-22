package cn.v5cn.netty.ws.pb.core.conn;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * @author ZYW
 */
public abstract class AbstractConn implements Conn {

    private final Serializable netId;
    private final ChannelHandlerContext ctx;

    public AbstractConn(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.netId = generateNetId(ctx);
        ctx.channel().attr(NET_ID).set(netId);
    }

    /**
     * 获取网络连接ID
     * @param ctx 通道上下文
     * @return 返回网络连接ID
     */
    protected abstract Serializable generateNetId(ChannelHandlerContext ctx);

    @Override
    public Serializable getNetId() {
        return this.netId;
    }

    @Override
    public ChannelHandlerContext getCtx() {
        return this.ctx;
    }

    @Override
    public ChannelFuture close() {
        return this.ctx.close();
    }
}
