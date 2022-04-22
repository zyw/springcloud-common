package cn.v5cn.netty.ws.pb.core.parser;

import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;
import cn.v5cn.netty.ws.pb.core.exception.ImLogicException;
import cn.v5cn.netty.ws.pb.core.function.ImBiConsumer;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyw
 */
public abstract class AbstractMsgParser implements MsgParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMsgParser.class);

    private final Map<Class<? extends Message>, ImBiConsumer<? extends Message, ChannelHandlerContext>> parserMap;

    protected AbstractMsgParser() {
        parserMap = new HashMap<>();
        registerParser();
    }

    public static void checkFrom(Message message, InternalMsg.Module module) {
        if(message instanceof InternalMsg) {
            InternalMsg msg = (InternalMsg) message;
            if(msg.getFrom() != module) {
                throw new ImLogicException("[unexpected msg] expect msg from: " + module.name() +
                        ", but received msg from: " + msg.getFrom().name() + "\n\rmsg: " + msg.toString());
            }
        }
    }

    public static void checkDest(Message message, InternalMsg.Module module) {
        if(message instanceof InternalMsg) {
            InternalMsg m = (InternalMsg) message;
            if(m.getDest() != module) {
                throw new ImLogicException("[unexpected msg] expect msg to: " + module.name() +
                        ", but received msg to: " + m.getFrom().name());
            }
        }
    }

    /**
     * 注册消息解析到Map中 key: Message Class value: ImBiConsumer
     * @param clazz Message Class
     * @param consumer ImBiConsumer
     * @param <T> extends Message
     */
    protected <T extends Message> void register(Class<T> clazz, ImBiConsumer<T, ChannelHandlerContext> consumer) {
        parserMap.put(clazz, consumer);
    }

    @Override
    public void parse(Message msg, ChannelHandlerContext ctx) {
        ImBiConsumer consumer = parserMap.get(msg.getClass());
        if(consumer == null) {
            LOGGER.warn("[message parser] unexpected msg: {}", msg.toString());
        }
        doParse(consumer, msg, msg.getClass(), ctx);
    }

    private <T extends Message> void doParse(ImBiConsumer<T,ChannelHandlerContext> consumer,Message msg,Class<T> clazz, ChannelHandlerContext ctx) {
        T m = clazz.cast(msg);
        try {
            consumer.accept(m, ctx);
        } catch (Exception e) {
            throw new ImLogicException("[msg parse] has error", e);
        }
    }

    /**
     * 注册msg处理方法
     */
    public abstract void registerParser();
}
