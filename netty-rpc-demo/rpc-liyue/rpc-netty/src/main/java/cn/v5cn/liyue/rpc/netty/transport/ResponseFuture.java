package cn.v5cn.liyue.rpc.netty.transport;

import cn.v5cn.liyue.rpc.netty.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 响应Future
 * @author LiYue
 * Date: 2019/9/20
 */
public class ResponseFuture {
    private final int requestId;
    private final CompletableFuture<Command> future;
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
