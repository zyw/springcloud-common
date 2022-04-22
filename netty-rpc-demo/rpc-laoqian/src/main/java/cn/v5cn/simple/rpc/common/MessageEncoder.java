package cn.v5cn.simple.rpc.common;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 响应消息的编码器
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 21:06
 */
@ChannelHandler.Sharable
public class MessageEncoder extends MessageToMessageEncoder<MessageOutput> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageOutput msg, List<Object> out) throws Exception {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer();
        writerStr(buf,msg.getRequestId());
        writerStr(buf,msg.getType());
        writerStr(buf, JSON.toJSONString(msg.getPayload()));
        out.add(buf);
    }

    private void writerStr(ByteBuf buf,String s) {
        buf.writeInt(s.length());
        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
    }
}
