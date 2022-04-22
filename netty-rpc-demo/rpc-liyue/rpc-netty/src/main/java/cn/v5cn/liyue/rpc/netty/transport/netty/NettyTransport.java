package cn.v5cn.liyue.rpc.netty.transport.netty;

import cn.v5cn.liyue.rpc.netty.transport.InFlightRequests;
import cn.v5cn.liyue.rpc.netty.transport.ResponseFuture;
import cn.v5cn.liyue.rpc.netty.transport.Transport;
import cn.v5cn.liyue.rpc.netty.transport.command.Command;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * Netty Transport实现类
 * @author LiYue
 * Date: 2019/9/20
 */
public class NettyTransport implements Transport {

    private final Channel channel;
    private final InFlightRequests inFlightRequests;

    NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public CompletableFuture<Command> send(Command request) {
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            // 将在途请求放到inFlightRequests中
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(),completableFuture));
            // 发送命令
            channel.writeAndFlush(request).addListener((ChannelFutureListener)channelFuture -> {
                // 处理发送失败的情况
                if(!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            // 处理发送异常
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }
}
