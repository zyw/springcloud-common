package cn.v5cn.rpc.lagou.transport;

import cn.v5cn.rpc.lagou.protocol.Message;
import cn.v5cn.rpc.lagou.protocol.Request;
import cn.v5cn.rpc.lagou.protocol.Response;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Connection implements Closeable {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    public static Map<Long,NettyResponseFuture<Response>> IN_FLIGHT_REQUEST_MAP = new ConcurrentHashMap<>();
    private ChannelFuture future;
    private AtomicBoolean isConnected = new AtomicBoolean();

    public Connection(ChannelFuture future, boolean isConnected) {
        this.future = future;
        this.isConnected.set(isConnected);
    }

    public NettyResponseFuture<Response> request(Message<Request> message, long timeOut) {
        // 生成并设置消息ID
        final long messageId = ID_GENERATOR.incrementAndGet();
        message.getHeader().setMessageId(messageId);
        // 创建消息关联的Future
        NettyResponseFuture responseFuture = new NettyResponseFuture(System.currentTimeMillis(),
                timeOut, message, future.channel(), new DefaultPromise(new DefaultEventLoop()));
        // 将消息ID和关联的Future记录到IN_FLIGHT_REQUEST_MAP集合中
        IN_FLIGHT_REQUEST_MAP.put(messageId, responseFuture);
        try {
            future.channel().writeAndFlush(message);
        }catch (Exception e) {
            IN_FLIGHT_REQUEST_MAP.remove(messageId);
            throw e;
        }
        return responseFuture;
    }

    @Override
    public void close() throws IOException {
        IN_FLIGHT_REQUEST_MAP.clear();
        ID_GENERATOR.set(0);
    }
}
