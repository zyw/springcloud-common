package cn.v5cn.netty.im.connector.start;

import cn.v5cn.netty.im.common.code.MsgDecoder;
import cn.v5cn.netty.im.common.code.MsgEncoder;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.connector.handler.ConnectorClientHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author yrw
 */
public class ConnectorServer {
    private static final Logger logger = LoggerFactory.getLogger(ConnectorServer.class);

    static void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<ServerChannel>() {
                    @Override
                    protected void initChannel(ServerChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("MsgDecoder", ConnectorStarter.injector.getInstance(MsgDecoder.class));
                        pipeline.addLast("MsgEncoder", ConnectorStarter.injector.getInstance(MsgEncoder.class));
                        pipeline.addLast("ConnectorClientHandler", ConnectorStarter.injector.getInstance(ConnectorClientHandler.class));
                    }
                });

        final ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                logger.info("[connector] start successfully at port {}, waiting for clients to connect...", port);
            } else {
                throw new ImException("[connector] start failed");
            }
        });

        try {
            f.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ImException("[connector] start failed", e);
        }
    }

}
