package cn.v5cn.rpc.lagou.codec;

import cn.v5cn.rpc.lagou.Constants;
import cn.v5cn.rpc.lagou.compress.Compressor;
import cn.v5cn.rpc.lagou.compress.CompressorFactory;
import cn.v5cn.rpc.lagou.protocol.Header;
import cn.v5cn.rpc.lagou.protocol.Message;
import cn.v5cn.rpc.lagou.protocol.Request;
import cn.v5cn.rpc.lagou.serialization.Serialization;
import cn.v5cn.rpc.lagou.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 请求解码
 * @author ZYW
 * @version 1.0
 * @date 2020-09-10 21:27
 */
public class RpcRequestDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 不到16字节的话无法解析消息头，暂不读取
        if (in.readableBytes() < Constants.HEADER_SIZE) {
            return;
        }
        // 记录当前readIndex指针的位置，方便重置
        in.markReaderIndex();
        // 尝试读取消息头的魔数部分
        short magic = in.readShort();
        if (magic != Constants.MAGIC) {
            // 重置readIndex指针
            in.resetReaderIndex();
            throw new RuntimeException("magic number error:" + magic);
        }
        // 依次读取消息版本、附加信息、消息ID以及消息体长度四部分

        byte version = in.readByte();
        byte extraInfo = in.readByte();
        long messageId = in.readLong();
        int size = in.readInt();
        Object request = null;
        // 心跳消息是没有消息体的，无需读取
        if (!Constants.isHeartBeat(extraInfo)) {
            // 对于非心跳消息，没有积累到足够的数据是无法进行反序列化的
            if (in.readableBytes() < size) {
                in.resetReaderIndex();
                return;
            }
            // 读取消息体并进行反序列化
            byte[] payload = new byte[size];
            in.readBytes(payload);
            // 这里根据消息头中的extraInfo部分选择相应的序列化和压缩方式
            Serialization serialization = SerializationFactory.get(extraInfo);
            Compressor compressor = CompressorFactory.get(extraInfo);

            // 经过解压和反序列化得到消息体
            request = serialization.deSerialize(compressor.unCompress(payload), Request.class);
        }

        // 将上面读取到的消息头和消息体拼装成完整的Message并向后传递
        Header header = new Header(magic,version,extraInfo,messageId,size);
        Message message = new Message(header,request);

        out.add(message);
    }
}
