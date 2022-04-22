package cn.v5cn.simple.rpc.common;

import com.alibaba.fastjson.JSON;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 17:01
 */
public class MessageInput {
    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息唯一ID
     */
    private String requestId;
    /**
     * 消息的json序列化字符串内容
     */
    private String payload;

    public MessageInput(String type, String requestId, String payload) {
        this.type = type;
        this.requestId = requestId;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public String getRequestId() {
        return requestId;
    }

    public <T> T getPayload(Class<T> clazz) {
        if(payload == null) {
            return null;
        }
        return JSON.parseObject(payload,clazz);
    }
}
