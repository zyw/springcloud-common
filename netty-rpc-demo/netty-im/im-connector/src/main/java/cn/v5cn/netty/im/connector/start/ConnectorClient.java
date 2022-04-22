package cn.v5cn.netty.im.connector.start;

import cn.v5cn.netty.im.common.code.MsgDecoder;
import cn.v5cn.netty.im.common.code.MsgEncoder;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.connector.handler.ConnectorTransferHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author yrw
 */
public class ConnectorClient {

    static void start(String[] transferUrls) {
        for (String transferUrl : transferUrls) {
            final String[] url = transferUrl.split(":");

            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            final ChannelFuture f = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            final ChannelPipeline p = ch.pipeline();
                            p.addLast("MsgDecoder", ConnectorStarter.injector.getInstance(MsgDecoder.class));
                            p.addLast("MsgEncoder", ConnectorStarter.injector.getInstance(MsgEncoder.class));
                            p.addLast("ConnectorTransferHandler", ConnectorStarter.injector.getInstance(ConnectorTransferHandler.class));
                        }
                    }).connect(url[0], Integer.parseInt(url[1]))
                    .addListener((ChannelFutureListener) future -> {
                        if (!future.isSuccess()) {
                            throw new ImException("[connector] connect to transfer failed! transfer url: " + transferUrl);
                        }
                    });

            try {
                f.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new ImException("[connector] connect to transfer failed! transfer url: " + transferUrl, e);
            }
        }
    }

}
