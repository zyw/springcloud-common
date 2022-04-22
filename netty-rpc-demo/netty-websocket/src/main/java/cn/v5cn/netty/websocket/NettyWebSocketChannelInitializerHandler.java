package cn.v5cn.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class NettyWebSocketChannelInitializerHandler extends ChannelInitializer<Channel> {

    private NettyWebSocketIMS ims;

    NettyWebSocketChannelInitializerHandler(NettyWebSocketIMS ims) {
        this.ims = ims;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new NettyWebSocketReadHandler(ims));
    }
}
