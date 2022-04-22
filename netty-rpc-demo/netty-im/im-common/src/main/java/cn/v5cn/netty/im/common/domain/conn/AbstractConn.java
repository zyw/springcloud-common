package cn.v5cn.netty.im.common.domain.conn;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * 连接的抽象实现
 * @author yrw
 */
public abstract class AbstractConn implements Conn {

    private final Serializable netId;
    private final ChannelHandlerContext ctx;

    public AbstractConn(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.netId = generateNetId(ctx);
        this.ctx.channel().attr(Conn.NET_ID).set(this.netId);
    }

    /**
     * 生成连接id
     * @param ctx
     * @return
     */
    protected abstract Serializable generateNetId(ChannelHandlerContext ctx);

    @Override
    public Serializable getNetId() {
        return netId;
    }

    @Override
    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    @Override
    public ChannelFuture close() {
        return ctx.close();
    }
}
