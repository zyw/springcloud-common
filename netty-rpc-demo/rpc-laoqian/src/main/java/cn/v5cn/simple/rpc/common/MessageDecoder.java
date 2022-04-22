package cn.v5cn.simple.rpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 消息解码器，使用Netty的ReplayingDecoder实现。简单起见，这里没有使用checkpoint去优化性能了，
 * 感兴趣的话读者可以参考一下我之前在公众号里发表的相关文章，将checkpoint相关的逻辑自己添加进去。
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 17:29
 */
public class MessageDecoder extends ReplayingDecoder<MessageInput> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String requestId = readStr(in);
        String type = readStr(in);
        String content = readStr(in);
        out.add(new MessageInput(type,requestId,content));
    }

    private String readStr(ByteBuf in) {
        // 字符串先长度后字节数组，统一UTF8编码
        int len = in.readInt();
        if(len < 0 || len > (1 << 20)) {
            throw new DecoderException("string too long len=" + len);
        }
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
