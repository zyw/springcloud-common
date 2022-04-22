package cn.v5cn.sb.netty.message.chat;

import cn.v5cn.sb.netty.common.dispacher.Message;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:25 下午
 */
public class ChatSendResponse implements Message {

    public static final String TYPE = "CHAT_SEND_RESPONSE";
    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;

    public String getMsgId() {
        return msgId;
    }

    public ChatSendResponse setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public ChatSendResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ChatSendResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "ChatSendResponse{" +
                "msgId='" + msgId + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
