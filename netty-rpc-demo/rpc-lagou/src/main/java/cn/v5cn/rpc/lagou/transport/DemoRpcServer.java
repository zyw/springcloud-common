package cn.v5cn.rpc.lagou.transport;

import cn.v5cn.rpc.lagou.codec.RpcRequestDecoder;
import cn.v5cn.rpc.lagou.codec.RpcResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class DemoRpcServer {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ServerBootstrap serverBootstrap;
    private Channel channel;
    protected int port;

    public DemoRpcServer(int port) {
        this.port = port;
        // 创建boss和worker两个EventLoopGroup，注意一些小细节，
        // workerGroup 是按照中的线程数是按照 CPU 核数计算得到的，
        bossGroup = NettyEventLoopFactory.eventLoopGroup(1,"boss");
        workerGroup = NettyEventLoopFactory.eventLoopGroup(Math.min(Runtime.getRuntime().availableProcessors() + 1,32),"worker");
        serverBootstrap = new ServerBootstrap().group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR,Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY,Boolean.TRUE)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 指定每个Channel上注册的ChannelHandler以及顺序
                        final ChannelPipeline p = ch.pipeline();
                        p.addLast("demp-rpc-decoder",new RpcRequestDecoder())
                         .addLast("demo-rpc-encoder",new RpcResponseEncoder())
                         .addLast("server-handler",new DemoRpcServerHandler());
                    }
                });
    }
    public ChannelFuture start() throws InterruptedException {
        ChannelFuture channelFuture = serverBootstrap.bind(port);
        channel = channelFuture.channel();
        channel.closeFuture();
        return channelFuture;
    }
}
