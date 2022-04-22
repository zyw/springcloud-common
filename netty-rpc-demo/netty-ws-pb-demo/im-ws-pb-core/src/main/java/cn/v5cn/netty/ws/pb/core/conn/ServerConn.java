package cn.v5cn.netty.ws.pb.core.conn;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * 连接实现
 * @author zyw
 */
public class ServerConn extends AbstractConn {

    public ServerConn(ChannelHandlerContext ctx) {
        super(ctx);
    }

    @Override
    protected Serializable generateNetId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(Conn.NET_ID).get();
    }
}
