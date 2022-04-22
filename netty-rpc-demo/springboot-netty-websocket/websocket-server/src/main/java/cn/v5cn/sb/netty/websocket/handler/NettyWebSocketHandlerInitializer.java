package cn.v5cn.sb.netty.websocket.handler;

import cn.v5cn.sb.netty.common.codec.WebSocketFrameInvocationCodec;
import cn.v5cn.sb.netty.common.dispacher.MessageDispatcher;
import cn.v5cn.sb.netty.server.handler.NettyServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyWebSocketHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Autowired
    private NettyServerHandler handler;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // websocket 相关的配置
        ChannelPipeline pipeline = ch.pipeline();
        // 日志 handler
        pipeline.addLast(new LoggingHandler())
            // 空闲检测
            .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
            //因为基于http协议，使用http的编码和解码器
            .addLast(new HttpServerCodec())
            //是以块方式写，添加ChunkedWriteHandler处理器
            .addLast(new ChunkedWriteHandler())
            /*
            说明
                1. http数据在传输过程中是分段, HttpObjectAggregator ，就是可以将多个段聚合
                2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
             */
            .addLast(new HttpObjectAggregator(8192))
            /*
            说明
                1. 对应websocket ，它的数据是以 帧(frame) 形式传递
                2. 可以看到WebSocketFrame 下面有六个子类
                3. 浏览器请求时 ws://localhost:8888/im 表示请求的uri
                4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
                5. 是通过一个 状态码 101
             */
            .addLast(new WebSocketServerProtocolHandler("/im"))
            /*
             * WebSocketFrame <---(相互转换)---> Invocation
             */
            .addLast(new WebSocketFrameInvocationCodec())
            // 消息分发器
            .addLast(messageDispatcher)
            // 自定义handler,处理业务逻辑
            .addLast(handler);
    }
}
