package cn.v5cn.netty.ws.pb.core.handler;

import cn.v5cn.netty.ws.pb.core.constant.MsgVersion;
import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;
import cn.v5cn.netty.ws.pb.core.util.IdWorker;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author ZYW
 */
public class ProcessMsgNode {
    private final Long id;

    private final InternalMsg.Module from;
    private final InternalMsg.Module dest;

    private final ChannelHandlerContext ctx;

    private final CompletableFuture<Void> future;

    private final Message message;

    private final Consumer<Message> consumer;

    public ProcessMsgNode(Long id,
                          InternalMsg.Module from,
                          InternalMsg.Module dest,
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
            InternalMsg ack = InternalMsg.newBuilder()
                    .setVersion(MsgVersion.V1.getVersion())
                    .setId(IdWorker.snowGenId())
                    .setFrom(from)
                    .setDest(dest)
                    .setCreateTime(System.currentTimeMillis())
                    .setMsgType(InternalMsg.MsgType.ACK)
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

    public InternalMsg.Module getFrom() {
        return this.from;
    }

    public InternalMsg.Module getDest() {
        return this.dest;
    }

    public ChannelHandlerContext getCtx() {
        return this.ctx;
    }
}
