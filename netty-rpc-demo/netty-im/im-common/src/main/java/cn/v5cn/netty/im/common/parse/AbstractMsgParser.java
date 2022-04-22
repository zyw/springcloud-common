package cn.v5cn.netty.im.common.parse;

import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.common.function.ImBiConsumer;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2019-05-18
 * Time: 16:17
 *
 * @author yrw
 */
public abstract class AbstractMsgParser {

    private final Logger logger = LoggerFactory.getLogger(AbstractMsgParser.class);

    private final Map<Class<? extends Message>, ImBiConsumer<? extends Message, ChannelHandlerContext>> parseMap;

    protected AbstractMsgParser() {
        this.parseMap = new HashMap<>();
        registerParsers();
    }

    public static void checkFrom(Message message, Internal.InternalMsg.Module module) {
        if(message instanceof Internal.InternalMsg) {
            Internal.InternalMsg msg = (Internal.InternalMsg) message;
            if(msg.getFrom() != module) {
                throw new ImException("[unexpected msg] expect msg from: " + module.name() +
                        ", but received msg from: " + msg.getFrom().name() + "\n\rmsg: " + msg.toString());
            }
        }
    }

    public static void checkDest(Message message, Internal.InternalMsg.Module module) {
        if (message instanceof Internal.InternalMsg) {
            Internal.InternalMsg m = (Internal.InternalMsg) message;
            if (m.getDest() != module) {
                throw new ImException("[unexpected msg] expect msg to: " + module.name() +
                        ", but received msg to: " + m.getFrom().name());
            }
        }
    }

    /**
     * 注册msg处理方法
     */
    public abstract void registerParsers();

    protected <T extends Message> void register(Class<T> clazz, ImBiConsumer<T, ChannelHandlerContext> consumer) {
        parseMap.put(clazz, consumer);
    }

    public void parse(Message msg, ChannelHandlerContext ctx) {
        ImBiConsumer consumer = parseMap.get(msg.getClass());
        if (consumer == null) {
            logger.warn("[message parser] unexpected msg: {}", msg.toString());
        }
        doParse(consumer, msg.getClass(), msg, ctx);
    }

    private <T extends Message> void doParse(ImBiConsumer<T, ChannelHandlerContext> consumer, Class<T> clazz, Message msg, ChannelHandlerContext ctx) {
        final T m = clazz.cast(msg);
        try {
            consumer.accept(m, ctx);
        } catch (Exception e) {
            throw new ImException("[msg parse] has error", e);
        }
    }

}
