package cn.v5cn.liyue.rpc.netty.transport.netty;

import cn.v5cn.liyue.rpc.netty.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 解码请求参数
 * @author LiYue
 * Date: 2019/9/20
 */
public class RequestDecoder extends CommandDecoder {
    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return new Header(
                byteBuf.readInt(),
                byteBuf.readInt(),
                byteBuf.readInt()
        );
    }
}
