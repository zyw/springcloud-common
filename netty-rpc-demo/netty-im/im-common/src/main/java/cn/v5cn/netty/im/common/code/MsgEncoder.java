package cn.v5cn.netty.im.common.code;

import cn.v5cn.netty.im.protobuf.constant.MsgTypeEnum;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yrw
 */
public class MsgEncoder extends MessageToByteEncoder<Message> {
    private static final Logger logger = LoggerFactory.getLogger(MsgEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        try {
            byte[] bytes = message.toByteArray();
            int code = MsgTypeEnum.getByClass(message.getClass()).getCode();
            int length = bytes.length;

            ByteBuf buf = Unpooled.buffer(8 + length);
            buf.writeInt(length);
            buf.writeInt(code);
            buf.writeBytes(bytes);
            out.writeBytes(buf);
        } catch (Exception e) {
            logger.error("[client] msg encode has error", e);
        }
    }
}
