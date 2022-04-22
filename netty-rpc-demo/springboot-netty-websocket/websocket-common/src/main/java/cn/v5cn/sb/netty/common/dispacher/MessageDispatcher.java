package cn.v5cn.sb.netty.common.dispacher;


import cn.v5cn.sb.netty.common.codec.Invocation;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;

/*@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Object> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageHandlerContainer messageHandlerContainer;

    public static final AttributeKey<String> CHANNEL_ATTR_KEY_SERVER_TYPE = AttributeKey.newInstance("serverType");

//    private final ExecutorService executor = Executors.newFixedThreadPool(200);

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("message-dispatcher-pool-%d").build();

    *//**
     * Common Thread Pool
     *//*
    private ExecutorService pool = new ThreadPoolExecutor(5, 200,0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object obj) throws Exception {
        Channel channel = ctx.channel();
        Invocation msg = null;
        if(obj instanceof Invocation) {
            msg = (Invocation)obj;
            channel.attr(CHANNEL_ATTR_KEY_SERVER_TYPE).set(ServerTypeEnums.TCP.name());
        } else if(obj instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame)obj;
            msg = JSON.parseObject(frame.text(), Invocation.class);
            channel.attr(CHANNEL_ATTR_KEY_SERVER_TYPE).set(ServerTypeEnums.WEBSOCKET.name());
        } else {
            logger.error("[MessageDispatcher][传入类型不正确][{}]",obj);
        }
        // <3.1> 获得 type 对应的 MessageHandler 处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(msg.getType());
        // 获得 MessageHandler 处理器的消息类
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        // <3.2> 解析消息
        Message message = JSON.parseObject(msg.getMessage(), messageClass);
        // <3.3> 执行逻辑
        pool.submit(() -> {
            messageHandler.execute(channel, message);
        });

    }*/

@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    @Autowired
    private MessageHandlerContainer messageHandlerContainer;

//    private final ExecutorService executor = Executors.newFixedThreadPool(200);

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("message-dispatcher-pool-%d").build();

    /*
     * Common Thread Pool
     */
    private ExecutorService pool = new ThreadPoolExecutor(5, 200,0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation msg) throws Exception {
        // <3.1> 获得 type 对应的 MessageHandler 处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(msg.getType());
        // 获得 MessageHandler 处理器的消息类
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        // <3.2> 解析消息
        Message message = JSON.parseObject(msg.getMessage(), messageClass);
        // <3.3> 执行逻辑
        pool.submit(() -> {
            messageHandler.execute(ctx.channel(), message);
        });
    }
}
