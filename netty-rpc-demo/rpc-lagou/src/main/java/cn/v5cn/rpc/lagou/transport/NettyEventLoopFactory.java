package cn.v5cn.rpc.lagou.transport;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class NettyEventLoopFactory {
    public static EventLoopGroup eventLoopGroup(int defaultIoThreads, String nettyClientWorker) {
        EventLoopGroup group;
        if(Epoll.isAvailable()) {
            group = new EpollEventLoopGroup(defaultIoThreads);
        } else {
            group = new NioEventLoopGroup(defaultIoThreads);
        }
        return group;
    }
}
