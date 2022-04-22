package cn.v5cn.liyue.rpc.netty.transport.netty;

import cn.v5cn.liyue.rpc.netty.transport.command.Command;
import cn.v5cn.liyue.rpc.netty.transport.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码响应参数
 * @author LiYue
 * Date: 2019/9/23
 */
public abstract class CommandEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if(!(o instanceof Command)){
            return;
        }
        Command command = (Command)o;
        byteBuf.writeInt(Integer.BYTES + command.getHeader().length() + command.getPayload().length);
        encodeHeader(channelHandlerContext,command.getHeader(),byteBuf);
        byteBuf.writeBytes(command.getPayload());
    }

    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header,ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(header.getType());
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getRequestId());
    }
}
