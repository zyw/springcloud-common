package cn.v5cn.netty.im.common.code;

import cn.v5cn.netty.im.common.parse.ParseService;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author yrw
 */
public class MsgDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MsgDecoder.class);

    private final ParseService parseService;

    public MsgDecoder() {
        this.parseService = new ParseService();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
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
            logger.error("[IM msg decoder]message length less than 0, channel closed");
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

        Message msg = parseService.getMsgByCode(code, body);
        out.add(msg);
    }
}
