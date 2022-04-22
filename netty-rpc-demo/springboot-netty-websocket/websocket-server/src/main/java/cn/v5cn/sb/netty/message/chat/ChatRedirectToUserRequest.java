package cn.v5cn.sb.netty.message.chat;

import cn.v5cn.sb.netty.common.dispacher.Message;

public class ChatRedirectToUserRequest implements Message {

    public static final String TYPE = "CHAT_REDIRECT_TO_USER_REQUEST";

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 发送消息用户
     */
    private String fromUser;
    /**
     * 内容
     */
    private String content;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatRedirectToUserRequest{" +
                "msgId='" + msgId + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
