package cn.v5cn.liyue.rpc.netty.client;

import cn.v5cn.liyue.rpc.netty.transport.Transport;

/**
 * @author LiYue
 * Date: 2019/9/27
 */
public interface ServiceStub {
    /**
     * 设置Transport
     * @param transport transport对象
     */
    void setTransport(Transport transport);
}
