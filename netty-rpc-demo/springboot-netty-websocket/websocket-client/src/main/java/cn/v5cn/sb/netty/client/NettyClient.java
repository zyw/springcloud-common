package cn.v5cn.sb.netty.client;

import cn.v5cn.sb.netty.client.handler.NettyClientHandlerInitializer;
import cn.v5cn.sb.netty.common.codec.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:21 下午
 */
@Component
public class NettyClient {
    /**
     * 重连频率，单位：秒
     */
    private static final Integer RECONNECT_SECONDS = 20;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${netty.server.host}")
    private String serverHost;
    @Value("${netty.server.port}")
    private Integer serverPort;

    @Autowired
    private NettyClientHandlerInitializer nettyClientHandlerInitializer;

    /**
     * 线程组，用于客户端对服务端的连接，数据读写
     */
    private EventLoopGroup eventGroup = new NioEventLoopGroup();
    /**
     * Netty Client Channel
     */
    private volatile Channel channel;

    /**
     * 启动 Netty Server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // <2.1> 创建 Bootstrap 对象，用于 Netty Client 启动
        Bootstrap bootstrap = new Bootstrap();
        // <2.2>
        // <2.2.1> 设置一个 EventLoopGroup 对象
        bootstrap.group(eventGroup)
                // <2.2.2> 指定 Channel 为客户端 NioSocketChannel
                .channel(NioSocketChannel.class)
                // <2.2.3> 指定连接服务器的地址
                .remoteAddress(serverHost, serverPort)
                // <2.2.4> TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .option(ChannelOption.SO_KEEPALIVE, true)
                // <2.2.5> 允许较小的数据包的发送，降低延迟
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(nettyClientHandlerInitializer);
        // <2.3> 连接服务器，并异步等待成功，即启动客户端
        bootstrap.connect().addListener((ChannelFutureListener) future -> {
            // 连接失败
            if(!future.isSuccess()) {
                logger.error("[start][Netty Client 连接服务器({}:{}) 失败]", serverHost, serverPort);
                reconnect();
                return;
            }
            // 连接成功
            channel = future.channel();
            logger.info("[start][Netty Client 连接服务器({}:{}) 成功]", serverHost, serverPort);
        });
    }

    public void reconnect() {
        eventGroup.schedule(() -> {
            logger.info("[reconnect][开始重连]");
            try {
                start();
            } catch (InterruptedException e) {
                logger.error("[reconnect][重连失败]", e);
            }
        }, RECONNECT_SECONDS, TimeUnit.SECONDS);

        logger.info("[reconnect][{} 秒后将发起重连]", RECONNECT_SECONDS);
    }

    /**
     * 关闭 Netty Server
     */
    @PreDestroy
    public void shutdown() {
        // <3.1> 关闭 Netty Client
        if (channel != null) {
            channel.close();
        }
        // <3.2> 优雅关闭一个 EventLoopGroup 对象
        eventGroup.shutdownGracefully();
    }

    /**
     * 发送消息
     *
     * @param invocation 消息体
     */
    public void send(Invocation invocation) {
        if (channel == null) {
            logger.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            logger.error("[send][连接{}未激活]", channel.id());
            return;
        }
        // 发送消息
        channel.writeAndFlush(invocation);
    }
}
