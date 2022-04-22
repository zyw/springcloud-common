package cn.v5cn.sb.netty.handler.heartbeat;

import cn.v5cn.sb.netty.common.dispacher.MessageHandler;
import cn.v5cn.sb.netty.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:30 下午
 */
@Component
public class HeartbeatResponseHandler implements MessageHandler<HeartbeatResponse> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, HeartbeatResponse message) {
        logger.info("[client]  [execute][收到连接({}) 的心跳响应]", channel.id());
    }

    @Override
    public String getType() {
        return HeartbeatResponse.TYPE;
    }
}
