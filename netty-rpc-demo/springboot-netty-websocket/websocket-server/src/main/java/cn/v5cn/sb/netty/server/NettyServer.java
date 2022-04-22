package cn.v5cn.sb.netty.server;

import org.springframework.stereotype.Component;

/**
 * 启动TCP、WebSocket服务
 * @author zyw
 */
public interface NettyServer {
    /**
     * 启动服务
     * @throws InterruptedException
     */
    void start() throws InterruptedException;

    /**
     * 停止服务
     */
    void shutdown();
}
