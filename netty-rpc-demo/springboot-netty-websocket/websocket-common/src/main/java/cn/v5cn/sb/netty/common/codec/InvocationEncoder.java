package cn.v5cn.sb.netty.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码器
 * @author 艿艿
 */
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void encode(ChannelHandlerContext context, Invocation invocation, ByteBuf byteBuf) throws Exception {
        // <2.1> 将 Invocation 转换成 byte[] 数组
        byte[] content = JSON.toJSONBytes(invocation);
        // <2.2> 写入 length
        byteBuf.writeInt(content.length);
        // <2.3> 写入 Redis
        byteBuf.writeBytes(content);
        logger.info("[encode][连接({}) 编码了一条消息({})]", context.channel().id(), invocation.toString());
    }
}
