package cn.v5cn.cim.server.init;

import cn.v5cn.cim.server.handle.CIMServerHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author crossoverJie
 */
public class CIMServerInitializer extends ChannelInitializer<Channel> {

    private final CIMServerHandle cimServerHandle = new CIMServerHandle();

    @Override
    protected void initChannel(Channel channel) throws Exception {

    }
}
