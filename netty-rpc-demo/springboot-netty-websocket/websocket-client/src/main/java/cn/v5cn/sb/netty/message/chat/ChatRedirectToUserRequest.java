package cn.v5cn.sb.netty.message.chat;

import cn.v5cn.sb.netty.common.dispacher.Message;

/**
 * 转发消息给一个用户的 Message
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:25 下午
 */
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

    public ChatRedirectToUserRequest setMsgId(String msgId) {
        this.msgId = msgId;
        return this;
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

    public ChatRedirectToUserRequest setContent(String content) {
        this.content = content;
        return this;
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
