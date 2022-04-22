package cn.v5cn.simple.rpc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型注册中心和消息处理器注册中心。
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 21:01
 */
public class MessageHandlers {
    private Map<String,IMessageHandler<?>> handlers = new HashMap<>();

    private IMessageHandler<MessageInput> defaultHandler;

    public void register(String type,IMessageHandler<?> handler) {
        handlers.put(type,handler);
    }

    public MessageHandlers defaultHandler(IMessageHandler<MessageInput> defaultHandler) {
        this.defaultHandler = defaultHandler;
        return this;
    }

    public IMessageHandler<MessageInput> defaultHandler() {
        return defaultHandler;
    }

    public IMessageHandler<?> get(String type) {
        IMessageHandler<?> handler = handlers.get(type);
        return handler;
    }
}
