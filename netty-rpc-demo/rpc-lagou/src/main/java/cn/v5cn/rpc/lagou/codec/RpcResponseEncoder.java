package cn.v5cn.rpc.lagou.codec;

import cn.v5cn.rpc.lagou.Constants;
import cn.v5cn.rpc.lagou.compress.Compressor;
import cn.v5cn.rpc.lagou.compress.CompressorFactory;
import cn.v5cn.rpc.lagou.protocol.Header;
import cn.v5cn.rpc.lagou.protocol.Message;
import cn.v5cn.rpc.lagou.serialization.Serialization;
import cn.v5cn.rpc.lagou.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-09-10 22:36
 */
public class RpcResponseEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        Header header = msg.getHeader();
        // 依次序列化消息头中的魔数、版本、附加信息以及消息ID
        out.writeShort(header.getMagic());
        out.writeByte(header.getVersion());
        out.writeByte(header.getExtraInfo());
        out.writeLong(header.getMessageId());
        Object content = msg.getPayload();
        if(Constants.isHeartBeat(header.getExtraInfo())) {
            // 心跳消息，没有消息体，这里写入0
            out.writeInt(0);
            return;
        }
        // 按照extraInfo部分指定的序列化方式和压缩方式进行处理
        Serialization serialization = SerializationFactory.get(header.getExtraInfo());
        Compressor compressor = CompressorFactory.get(header.getExtraInfo());

        byte[] payload = compressor.compress(serialization.serialize(content));

        // 写入消息体长度
        out.writeInt(payload.length);
        // 写入消息体
        out.writeBytes(payload);
    }
}
