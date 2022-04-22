package cn.v5cn.netty.im.common.domain.conn;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * 连接实现
 * @author yrw
 */
public class ConnectorConn extends AbstractConn {

    public ConnectorConn(ChannelHandlerContext ctx) {
        super(ctx);
    }

    @Override
    protected Serializable generateNetId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(Conn.NET_ID).get();
    }
}
