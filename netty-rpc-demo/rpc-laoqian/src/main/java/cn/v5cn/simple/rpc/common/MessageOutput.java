package cn.v5cn.simple.rpc.common;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 17:11
 */
public class MessageOutput {
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
    private Object payload;

    public MessageOutput(String requestId, String type, Object payload) {
        this.requestId = requestId;
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return this.type;
    }

    public String getRequestId() {
        return requestId;
    }

    public Object getPayload() {
        return payload;
    }
}
