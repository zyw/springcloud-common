package cn.v5cn.simple.rpc.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理器接口，每个自定义服务必须实现handle方法
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 17:46
 */
@FunctionalInterface
public interface IMessageHandler<T> {
    void handle(ChannelHandlerContext ctx,String requestId,T message);
}
