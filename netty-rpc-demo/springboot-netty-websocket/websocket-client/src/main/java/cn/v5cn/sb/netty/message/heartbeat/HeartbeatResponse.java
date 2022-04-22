package cn.v5cn.sb.netty.message.heartbeat;

import cn.v5cn.sb.netty.common.dispacher.Message;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:27 下午
 */
public class HeartbeatResponse implements Message {
    /**
     * 类型 - 心跳响应
     */
    public static final String TYPE = "HEARTBEAT_RESPONSE";

    @Override
    public String toString() {
        return "HeartbeatResponse{}";
    }
}
