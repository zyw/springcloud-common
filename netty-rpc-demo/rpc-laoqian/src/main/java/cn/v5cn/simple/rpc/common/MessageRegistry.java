package cn.v5cn.simple.rpc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型注册中心和消息处理器注册中心。
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 20:58
 */
public class MessageRegistry {
    private Map<String,Class<?>> clazzes = new HashMap<>();

    public void register(String type,Class<?> clazz) {
        clazzes.put(type,clazz);
    }

    public Class<?> get(String type) {
        return clazzes.get(type);
    }
}
