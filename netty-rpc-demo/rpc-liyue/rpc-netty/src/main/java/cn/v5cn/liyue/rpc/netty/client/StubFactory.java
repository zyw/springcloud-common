package cn.v5cn.liyue.rpc.netty.client;

import cn.v5cn.liyue.rpc.netty.transport.Transport;

/**
 * 客户端桩接口
 * @author LiYue
 * Date: 2019/9/27
 */
public interface StubFactory {
    /**
     * 创建客户端Stub接口，通过实现该接口，可以有多种方式动态创建Stub
     * @param transport
     * @param serviceClass
     * @param <T>
     * @return
     */
    <T> T createStub(Transport transport,Class<T> serviceClass);
}
