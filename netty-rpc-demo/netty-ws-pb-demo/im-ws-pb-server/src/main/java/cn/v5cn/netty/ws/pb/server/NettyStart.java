package cn.v5cn.netty.ws.pb.server;

import cn.v5cn.netty.ws.pb.core.codec.MsgWebSocketCodec;
import cn.v5cn.netty.ws.pb.core.exception.ImLogicException;
import cn.v5cn.netty.ws.pb.server.handler.WsPbServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author zyw
 */
public class NettyStart {

    private static final Logger logger = LoggerFactory.getLogger(NettyStart.class);

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    static void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();;
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 设置TCP接收缓冲区大小（字节数）
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    // 服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值，Windows为200，其他为128
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 设置禁用nagle算法，如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new MyChannelInitializer())
                    .bind(new InetSocketAddress(port))
                    .addListeners((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            logger.info("[connector] start successfully at port {}, waiting for clients to connect...", port);
                        } else {
                            throw new ImLogicException("[connector] start failed");
                        }
                    })
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();

        } catch (InterruptedException e) {
            throw new ImLogicException("[connector] start failed", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static class MyChannelInitializer extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // 日志 handler
            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            // 空闲检测
            pipeline.addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS));
            //因为基于http协议，使用http的编码和解码器
            pipeline.addLast(new HttpServerCodec());
            //是以块方式写，添加ChunkedWriteHandler处理器
            pipeline.addLast(new ChunkedWriteHandler());
            /*
            说明
                1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
                2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
             */
            pipeline.addLast(new HttpObjectAggregator(8192));
            /*
            说明
                1. 对应websocket ，它的数据是以 帧(frame) 形式传递
                2. 可以看到WebSocketFrame 下面有六个子类
                3. 浏览器请求时 ws://localhost:8888/im 表示请求的uri
                4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
                5. 是通过一个 状态码 101
             */
            pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
            pipeline.addLast("MsgWebSocketCodec", ImWsPbServer.injector.getInstance(MsgWebSocketCodec.class));
            pipeline.addLast("WsPbServerHandler", ImWsPbServer.injector.getInstance(WsPbServerHandler.class));
        }
    }
}
