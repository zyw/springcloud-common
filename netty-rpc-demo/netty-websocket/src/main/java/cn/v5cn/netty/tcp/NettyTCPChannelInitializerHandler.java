package cn.v5cn.netty.tcp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class NettyTCPChannelInitializerHandler extends ChannelInitializer<Channel> {

    private NettyTCPIMS ims;

    NettyTCPChannelInitializerHandler(NettyTCPIMS ims) {
        this.ims = ims;
    }


    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new NettyTCPReadHandler(this.ims));
    }
}
