package cn.v5cn.rpc.lagou.transport;

import cn.v5cn.rpc.lagou.Constants;
import cn.v5cn.rpc.lagou.protocol.Message;
import cn.v5cn.rpc.lagou.protocol.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DemoRpcServerHandler extends SimpleChannelInboundHandler<Message<Request>> {

    static Executor executor = Executors.newCachedThreadPool();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<Request> msg) throws Exception {
        final byte extraInfo = msg.getHeader().getExtraInfo();
        // 心跳消息，直接返回即可
        if(Constants.isHeartBeat(extraInfo)) {
            ctx.writeAndFlush(msg);
            return;
        }
        // 非心跳消息，直接封装成Runnable提交到业务线程
        executor.execute(new InvokeRunnable(ctx,msg));
    }
}
