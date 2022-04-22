package cn.v5cn.sb.netty.tcp.handler;

import cn.v5cn.sb.netty.common.codec.InvocationDecoder;
import cn.v5cn.sb.netty.common.codec.InvocationEncoder;
import cn.v5cn.sb.netty.common.dispacher.MessageDispatcher;
import cn.v5cn.sb.netty.server.handler.NettyServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // <1> 获得 Channel 对应的 ChannelPipeline
        ChannelPipeline pipeline = channel.pipeline();
        // <2> 添加一堆 NettyServerHandler 到 ChannelPipeline 中
        pipeline
                // 日志 handler
                .addLast(new LoggingHandler())
                // 空闲检测
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                // 编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // 消息分发器
                .addLast(messageDispatcher)
                // 服务端处理器
                .addLast(nettyServerHandler);
    }
}
