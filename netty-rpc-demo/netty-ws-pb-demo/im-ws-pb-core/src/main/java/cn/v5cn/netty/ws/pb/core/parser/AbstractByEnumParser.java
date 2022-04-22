package cn.v5cn.netty.ws.pb.core.parser;

import cn.v5cn.netty.ws.pb.core.function.ImBiConsumer;
import com.google.protobuf.Message;
import com.google.protobuf.ProtocolMessageEnum;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author ZYW
 */
public abstract class AbstractByEnumParser<E extends ProtocolMessageEnum, M extends Message> {

    private final Map<E, ImBiConsumer<M, ChannelHandlerContext>> parseMsp;

    public AbstractByEnumParser(int size) {
        this.parseMsp = new HashMap<>(size);
    }

    public void register(E type, ImBiConsumer<M, ChannelHandlerContext> consumer) {
        parseMsp.put(type, consumer);
    }

    /**
     * 解析器类型
     * @param msg 类型消息
     * @return 返回类型
     */
    protected abstract E getType(M msg);

    public ImBiConsumer<M, ChannelHandlerContext> generateFun() {

        return (m, ctx) -> Optional.ofNullable(parseMsp.get(getType(m)))
                .orElseThrow(() -> new IllegalArgumentException("Invalid msg enum " + m.toString()))
                .accept(m, ctx);
    }
}
