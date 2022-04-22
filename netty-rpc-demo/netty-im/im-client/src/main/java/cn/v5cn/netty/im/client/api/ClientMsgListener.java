package cn.v5cn.netty.im.client.api;

import cn.v5cn.netty.im.protobuf.generate.Chat;
import io.netty.channel.ChannelHandlerContext;

public interface ClientMsgListener {

    /**
     * do when the client connect to connector successfully
     * 当客户端成功连接到连接器时执行此操作
     */
    void online();

    /**
     * read a msg
     * @param chatMsg chatMsg
     */
    void read(Chat.ChatMsg chatMsg);

    /**
     * do when a msg has been sent
     * 发送消息后执行此操作
     * @param id id chatMsg msg id
     */
    void hasSent(Long id);

    /**
     * do when a msg has been delivered
     * 当消息已传递时执行此操作
     * @param id id chatMsg msg id
     */
    void hasDelivered(Long id);

    /**
     * do when a message has been read
     * 阅读邮件后执行此操作
     * @param id id chatMsg msg id
     */
    void hasRead(Long id);

    /**
     * do when the client disconnect to connector
     * 当客户端断开与连接器的连接时执行此操作
     */
    void offline();

    /**
     * a exception is occurred
     * 发生异常
     * @param ctx
     * @param cause
     */
    void hasException(ChannelHandlerContext ctx, Throwable cause);
}
