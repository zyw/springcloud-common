package cn.v5cn.netty.ws.pb.core.codec;

import cn.v5cn.netty.ws.pb.core.enums.MsgTypeEnum;
import cn.v5cn.netty.ws.pb.core.parser.MsgParserService;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author powertime
 */
public class MsgWebSocketCodec extends MessageToMessageCodec<BinaryWebSocketFrame, Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgWebSocketCodec.class);

    private final MsgParserService parserService;

    public MsgWebSocketCodec() {
        this.parserService = new MsgParserService();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> list) throws Exception {
        byte[] bytes = msg.toByteArray();
        int code = MsgTypeEnum.getByClass(msg.getClass()).getCode();
        int length = bytes.length;

        ByteBuf buffer = Unpooled.buffer(length + 8);
        buffer.writeInt(length);
        buffer.writeInt(code);
        buffer.writeBytes(bytes);

        list.add(new BinaryWebSocketFrame(buffer));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame bwf, List<Object> out) throws Exception {
        ByteBuf in = bwf.content();

        // 传输协议
        // 1. 主题字节长度（int类型）
        // 2. 主题类型码（int类型）
        // 3. 主题字节（byte[]类型）

        // 标记为读
        in.markReaderIndex();

        // readableBytes()返回可读字节数
        if(in.readableBytes() < 4) {
            in.resetReaderIndex();
            return;
        }

        int length = in.readInt();

        if(length < 0) {
            ctx.close();
            LOGGER.error("[IM消息解码]消息长度小于0, 关闭通道。");
            return;
        }

        //剩余可读长度-4小于length重置读取索引
        if(length > in.readableBytes() - 4) {
            //重新定位到此缓冲区中标记的 readerIndex
            in.resetReaderIndex();
            return;
        }

        int code = in.readInt();
        ByteBuf byteBuf = Unpooled.buffer(length);

        in.readBytes(byteBuf);

        byte[] body = byteBuf.array();

        Message msg = parserService.getMsgByCode(code, body);

        out.add(msg);
    }
}
