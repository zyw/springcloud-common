package cn.v5cn.netty.ws.pb.core.parser;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zyw
 */
public interface MsgParser {
    /**
     * 消息解析方法
     * @param msg im 消息
     * @param ctx netty上下文
     */
    void parse(Message msg, ChannelHandlerContext ctx);
}
