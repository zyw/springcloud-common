package cn.v5cn.liyue.rpc.netty.server;

/**
 *  注册服务提供者
 * @author LiYue
 * Date: 2019/9/29
 */
public interface ServiceProviderRegistry {
    /**
     * 添加服务提供者
     * @param serviceClass 服务类
     * @param serviceProvider 服务提供者类
     * @param <T>
     */
    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);
}
