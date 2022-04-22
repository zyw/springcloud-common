package cn.v5cn.netty.ws.pb.core.conn;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.io.Serializable;

/**
 * 连接
 * @author zyw
 */
public interface Conn {
    /**
     * 网络连接ID
     */
    AttributeKey<Serializable> NET_ID = AttributeKey.valueOf("netId");

    /**
     * 获取连接id
     * @return 网络连接ID
     */
    Serializable getNetId();

    /**
     * 返回ChannelHandlerContext
     * @return 通道上下文
     */
    ChannelHandlerContext getCtx();

    /**
     * 关闭通道连接
     * @return 通道future
     */
    ChannelFuture close();
}
