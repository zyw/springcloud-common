package cn.v5cn.rpc.lagou.transport;

import cn.v5cn.rpc.lagou.Constants;
import cn.v5cn.rpc.lagou.protocol.Message;
import cn.v5cn.rpc.lagou.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DemoRpcClientHandler extends SimpleChannelInboundHandler<Message<Response>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message<Response> msg) throws Exception {
        final NettyResponseFuture<Response> responseFuture = Connection.IN_FLIGHT_REQUEST_MAP.remove(msg.getHeader().getMessageId());
        Response response = msg.getPayload();
        // 心跳消息特殊处理
        if(response == null && Constants.isHeartBeat(msg.getHeader().getExtraInfo())) {
            response = new Response();
            response.setCode(Constants.HEARTBEAT_CODE);
        }
        responseFuture.getPromise().setSuccess(response);
    }
}
