package cn.v5cn.cim.common.kit;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author crossoverJie
 */
public interface HeartBeatHandler {
    /**
     * 处理心跳
     * @param ctx
     * @throws Exception
     */
    void process(ChannelHandlerContext ctx) throws Exception;
}
