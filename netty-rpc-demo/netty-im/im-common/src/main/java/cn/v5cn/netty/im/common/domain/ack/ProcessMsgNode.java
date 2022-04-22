package cn.v5cn.netty.im.common.domain.ack;

import cn.v5cn.netty.im.common.domain.constant.MsgVersion;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * msg need to be processed from server
 * 需要从服务器处理消息
 * Date: 2019-09-08
 * Time: 20:55
 *
 * @author yrw
 */
public class ProcessMsgNode {

    private final Long id;
    private final Internal.InternalMsg.Module from;
    private final Internal.InternalMsg.Module dest;
    private final ChannelHandlerContext ctx;

    private final CompletableFuture<Void> future;

    private final Message message;

    private final Consumer<Message> consumer;

    public ProcessMsgNode(Long id,
                          Internal.InternalMsg.Module from,
                          Internal.InternalMsg.Module dest,
                          ChannelHandlerContext ctx,
                          Message message,
                          Consumer<Message> consumer) {
        this.id = id;
        this.from = from;
        this.dest = dest;
        this.ctx = ctx;
        this.message = message;
        this.consumer = consumer;
        this.future = new CompletableFuture<>();
    }

    public Void process() {
        this.consumer.accept(this.message);
        return null;
    }

    public void sendAck() {
        if(ctx.channel().isOpen()) {
            Internal.InternalMsg ack = Internal.InternalMsg.newBuilder()
                    .setVersion(MsgVersion.V1.getVersion())
                    .setId(IdWorker.snowGenId())
                    .setFrom(from)
                    .setDest(dest)
                    .setCreateTime(System.currentTimeMillis())
                    .setMsgType(Internal.InternalMsg.MsgType.ACK)
                    .setMsgBody(id + "")
                    .build();

            ctx.writeAndFlush(ack);
        }
    }

    public void complete() {
        this.future.complete(null);
    }

    public CompletableFuture<Void> getFuture() {
        return this.future;
    }

    public Long getId() {
        return this.id;
    }

    public Internal.InternalMsg.Module getFrom() {
        return this.from;
    }

    public Internal.InternalMsg.Module getDest() {
        return this.dest;
    }

    public ChannelHandlerContext getCtx() {
        return this.ctx;
    }

}
