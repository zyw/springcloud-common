package cn.v5cn.cim.client.service.impl;

import cn.v5cn.cim.client.client.CIMClient;
import cn.v5cn.cim.client.thread.ContextHolder;
import cn.v5cn.cim.common.kit.HeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author crossoverJie
 */
@Service
public class ClientHeartBeatHandlerImpl implements HeartBeatHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHeartBeatHandlerImpl.class);

    @Autowired
    private CIMClient cimClient;

    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {

        // 重连
        ContextHolder.setReconnect(true);
        cimClient.reconnect();
    }
}
