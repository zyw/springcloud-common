package cn.v5cn.liyue.rpc.netty.transport.netty;

import cn.v5cn.liyue.rpc.netty.transport.command.Command;
import cn.v5cn.liyue.rpc.netty.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 命令解码器
 * @author LiYue
 * Date: 2019/9/23
 */
public abstract class CommandDecoder extends ByteToMessageDecoder {

    private static final int LENGTH_FIELD_LENGTH = Integer.BYTES;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(!byteBuf.isReadable(LENGTH_FIELD_LENGTH)) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt() - LENGTH_FIELD_LENGTH;
        if(byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }
        Header header = decodeHeader(channelHandlerContext, byteBuf);
        int payloadLength = length - header.length();
        byte[] payload = new byte[payloadLength];
        byteBuf.readBytes(payload);
        list.add(new Command(header,payload));
    }

    /**
     * 解码请求头
     * @param channelHandlerContext 通道出来上下文
     * @param byteBuf byte Buffer
     * @return 返回Header对象
     */
    protected abstract Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);
}
