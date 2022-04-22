package cn.v5cn.simple.rpc.client;

import cn.v5cn.simple.rpc.common.MessageInput;
import cn.v5cn.simple.rpc.common.MessageOutput;
import cn.v5cn.simple.rpc.common.MessageRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 22:41
 */
public class MessageCollector extends ChannelInboundHandlerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(MessageCollector.class);

    private MessageRegistry registry;
    private RPCClient client;
    private ChannelHandlerContext context;
    private ConcurrentMap<String,RPCFuture<?>> pendingTasks = new ConcurrentHashMap<>();

    private Throwable connectionClosed = new Exception("rpc connection not active error");

    public MessageCollector(MessageRegistry registry,RPCClient client) {
        this.registry = registry;
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.context = null;
        pendingTasks.forEach((__,future) -> {
            future.fail(connectionClosed);
        });

        pendingTasks.clear();
        // 尝试重连
        ctx.channel().eventLoop().schedule(() -> {
            client.reconnect();
        },1, TimeUnit.SECONDS);
    }

    public <T> RPCFuture<T> send(MessageOutput output) {
        ChannelHandlerContext ctx = context;
        RPCFuture<T> future = new RPCFuture<>();
        if(ctx != null) {
            ctx.channel().eventLoop().execute(() -> {
                pendingTasks.put(output.getRequestId(),future);
                ctx.writeAndFlush(output);
            });
        } else {
            future.fail(connectionClosed);
        }
        return future;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof MessageInput)) {
            return;
        }
        MessageInput input = (MessageInput) msg;
        // 业务逻辑在这里
        Class<?> clazz = registry.get(input.getType());
        if(clazz == null) {
            LOG.error("unrecognized msg type {}", input.getType());
            return;
        }
        Object o = input.getPayload(clazz);
        RPCFuture<Object> future = (RPCFuture<Object>) pendingTasks.remove(input.getRequestId());
        if(future == null) {
            LOG.error("future not found with type {}", input.getType());
            return;
        }
        future.success(o);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    public void close() {
        ChannelHandlerContext ctx = context;
        if(ctx != null) {
            ctx.close();
        }
    }
}
