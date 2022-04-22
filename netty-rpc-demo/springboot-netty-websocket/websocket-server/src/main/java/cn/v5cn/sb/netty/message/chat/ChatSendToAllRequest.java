package cn.v5cn.sb.netty.message.chat;

import cn.v5cn.sb.netty.common.dispacher.Message;

public class ChatSendToAllRequest implements Message {

    public static final String TYPE = "CHAT_SEND_TO_ALL_REQUEST";

    /**
     * 发消息用户
     */
    private String fromUser;
    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String content;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatSendToAllRequest{" +
                "fromUser='" + fromUser + '\'' +
                ", msgId='" + msgId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
