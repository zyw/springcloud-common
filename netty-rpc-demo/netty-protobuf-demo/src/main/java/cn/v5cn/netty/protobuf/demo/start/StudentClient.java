package cn.v5cn.netty.protobuf.demo.start;

import cn.v5cn.netty.protobuf.demo.handler.StudentClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author powertime
 */
public class StudentClient {

    private static final String HOST = "127.0.0.1";
    private static final Integer PORT = 8866;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new StudentClientInitializer());
            // 建立连接
            Channel ch = b.connect(HOST, PORT).sync().channel();
            // 等待关门
            ch.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
