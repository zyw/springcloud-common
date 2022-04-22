package cn.v5cn.netty.ws.pb.server.domain;

import cn.v5cn.netty.ws.pb.core.conn.AbstractConn;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zyw
 */
public class ClientConn extends AbstractConn {

    private static final AtomicLong NETID_GENERATOR = new AtomicLong(0);

    private String userId;

    public ClientConn(ChannelHandlerContext ctx) {
        super(ctx);
    }

    @Override
    protected Serializable generateNetId(ChannelHandlerContext ctx) {
        return NETID_GENERATOR.getAndIncrement();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
