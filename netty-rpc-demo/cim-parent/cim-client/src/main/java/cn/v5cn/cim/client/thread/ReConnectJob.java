package cn.v5cn.cim.client.thread;

import cn.v5cn.cim.client.service.impl.ClientHeartBeatHandlerImpl;
import cn.v5cn.cim.client.util.SpringBeanFactory;
import cn.v5cn.cim.common.kit.HeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author crossoverJie
 */
public class ReConnectJob implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReConnectJob.class);

    private ChannelHandlerContext context ;

    private HeartBeatHandler heartBeatHandler;

    public ReConnectJob(ChannelHandlerContext context) {
        this.context = context;
        this.heartBeatHandler = SpringBeanFactory.getBean(ClientHeartBeatHandlerImpl.class);
    }

    @Override
    public void run() {
        try {
            heartBeatHandler.process(context);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
    }
}
