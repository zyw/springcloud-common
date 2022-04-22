package cn.v5cn.netty.ws.pb.core.conn;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * 存储连接的容器
 * @author zyw
 */
public interface ConnContext<C extends Conn> {

    /**
     * 获取连接
     * @param ctx 通道上下文
     * @return 返回连接容器
     */
    C getConn(ChannelHandlerContext ctx);

    /**
     * 获取连接
     * @param netId 网络ID
     * @return 返回连接容器
     */
    C getConn(Serializable netId);

    /**
     * 添加连接
     * @param conn 连接
     */
    void addConn(C conn);

    /**
     * 删除连接
     * @param netId
     */
    void removeConn(Serializable netId);

    /**
     * 删除连接
     * @param ctx
     */
    void removeConn(ChannelHandlerContext ctx);

    /**
     * 删除所有连接
     */
    void removeAllConn();
}
