package cn.v5cn.rpc.lagou.transport;

import cn.v5cn.rpc.lagou.Constants;
import cn.v5cn.rpc.lagou.codec.RpcRequestDecoder;
import cn.v5cn.rpc.lagou.codec.RpcResponseEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.Closeable;
import java.io.IOException;

public class DemoRpcClient implements Closeable {

    protected Bootstrap clientBootstrap;
    protected EventLoopGroup group;
    private String host;
    private int port;

    public DemoRpcClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        clientBootstrap = new Bootstrap();
        // 创建并配置客户端Bootstrap
        group = NettyEventLoopFactory.eventLoopGroup(Constants.DEFAULT_IO_THREADS,"NettyClientWorker");
        clientBootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .channel(NioSocketChannel.class)
                //指定ChannelHandler的顺序
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("demo-rpc-encoder",new RpcResponseEncoder());
                        ch.pipeline().addLast("demo-rpc-decoder",new RpcRequestDecoder());
                        ch.pipeline().addLast("client-handler",new DemoRpcClientHandler());
                    }
                });
    }

    // 连接指定的地址和端口
    public ChannelFuture connect() {
        final ChannelFuture connect = clientBootstrap.connect(host, port);
        connect.awaitUninterruptibly();
        return connect;
    }

    @Override
    public void close() throws IOException {
        group.shutdownGracefully();
    }
}
