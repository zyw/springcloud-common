package cn.v5cn.liyue.rpc.netty.transport;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * @author LiYue
 * Date: 2019/9/25
 */
public interface TransportClient extends Closeable {

    /**
     * 创建Transport发送消息
     * @param address 注册中心地址
     * @param connectionTimeout 超时时间
     * @return 返回 Transport对象
     * @throws InterruptedException
     * @throws TimeoutException
     */
    Transport createTransport(SocketAddress address,long connectionTimeout) throws InterruptedException, TimeoutException;

    @Override
    void close();
}
