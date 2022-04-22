package cn.v5cn.sb.netty.message.heartbeat;

import cn.v5cn.sb.netty.common.dispacher.Message;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:26 下午
 */
public class HeartbeatRequest implements Message {
    /**
     * 类型 - 心跳请求
     */
    public static final String TYPE = "HEARTBEAT_REQUEST";

    @Override
    public String toString() {
        return "HeartbeatRequest{}";
    }
}
