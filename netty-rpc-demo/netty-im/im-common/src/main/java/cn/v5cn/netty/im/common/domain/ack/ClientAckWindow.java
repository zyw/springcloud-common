package cn.v5cn.netty.im.common.domain.ack;

import cn.v5cn.netty.im.common.domain.constant.MsgVersion;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.common.util.IdWorker;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * for client, every connection should has an ServerAckWindow
 * 对于客户端，每个连接都应该有一个ServerAckWindow
 * Date: 2019-09-08
 * Time: 20:42
 *
 * @author yrw
 */
public class ClientAckWindow {
    private static final Logger logger = LoggerFactory.getLogger(ClientAckWindow.class);

    private final int maxSize;

    private final AtomicBoolean first;
    private final AtomicLong lastId;
    private final ConcurrentMap<Long,ProcessMsgNode> notContinuousMap;

    public ClientAckWindow(int maxSize) {
        this.first = new AtomicBoolean(true);
        this.maxSize = maxSize;
        this.lastId = new AtomicLong(-1);
        this.notContinuousMap = new ConcurrentHashMap<>();
    }

    /**
     * multi thread do it
     *
     * @param id              msg id
     * @param from            from module
     * @param dest            dest module
     * @param receivedMsg
     * @param processFunction
     */
    public CompletableFuture<Void> offer(Long id, Internal.InternalMsg.Module from,
                                         Internal.InternalMsg.Module dest,
                                         ChannelHandlerContext ctx, Message receivedMsg,
                                         Consumer<Message> processFunction) {
        // 消息重复
        if(isRepeat(id)) {
            ctx.writeAndFlush(getInternalAck(id, from, dest));
            CompletableFuture<Void> future = new CompletableFuture<>();
            future.complete(null);
            return future;
        }

        ProcessMsgNode msgNode = new ProcessMsgNode(id, from, dest, ctx, receivedMsg, processFunction);
        // 消息不连续
        if(!isContinuous(id)) {
            if(notContinuousMap.size() >= maxSize) {
                CompletableFuture<Void> future = new CompletableFuture<>();
                future.completeExceptionally(new ImException("client window is full"));
                return future;
            }
            notContinuousMap.put(id, msgNode);
            return msgNode.getFuture();
        }
        // 处理消息
        return processAsync(msgNode);
    }

    private CompletableFuture<Void> processAsync(ProcessMsgNode msgNode) {
        return CompletableFuture
                .runAsync(msgNode::process)
                .thenAccept(v -> {
                    msgNode.sendAck();
                    msgNode.complete();
                })
                .thenAccept(v -> {
                    lastId.set(msgNode.getId());
                    notContinuousMap.remove(msgNode.getId());
                })
                .thenComposeAsync(v -> {
                    Long nextId = nextId(msgNode.getId());
                    if(notContinuousMap.containsKey(nextId)) {
                        // there is a next msg waiting in the map
                        // 队列中有下个消息
                        ProcessMsgNode nextNode = notContinuousMap.get(nextId);
                        return processAsync(nextNode);
                    } else {
                        // that's the newest msg
                        // 队列中没有下个消息
                        return msgNode.getFuture();
                    }
                })
                .exceptionally(e -> {
                    logger.error("[process received msg] has error", e);
                    return null;
                });
    }

    private boolean isRepeat(Long msgId) {
        return msgId <= lastId.get();
    }

    private boolean isContinuous(Long msgId) {
        //如果是本次会话的第一条消息
        if(first.compareAndSet(true, false)) {
            return true;
        } else {
            //不是第一条消息，则按照公式算（如果同时有好几条第一条消息，除了真正的第一条，其他会返回false）
            return msgId - lastId.get() == 1;
        }
    }

    private Long nextId(Long id){
        return  id + 1;
    }

    private Internal.InternalMsg getInternalAck(Long msgId, Internal.InternalMsg.Module from, Internal.InternalMsg.Module dest) {
        return Internal.InternalMsg.newBuilder()
                .setVersion(MsgVersion.V1.getVersion())
                .setId(IdWorker.snowGenId())
                .setFrom(from)
                .setDest(dest)
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(Internal.InternalMsg.MsgType.ACK)
                .setMsgBody(msgId + "")
                .build();
    }
}
