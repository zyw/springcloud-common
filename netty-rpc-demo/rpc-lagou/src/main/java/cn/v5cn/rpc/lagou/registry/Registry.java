package cn.v5cn.rpc.lagou.registry;

import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

/**
 * 服务注册接口
 */
public interface Registry<T> {
    /**
     * 注册服务到Zookeeper
     * @param si
     */
    void registryService(ServiceInstance<T> si) throws Exception;

    /**
     * 取消注册到Zookeeper
     * @param si
     */
    void unRegistryService(ServiceInstance<T> si) throws Exception;

    /**
     * 查询服务
     * @param name
     * @return
     */
    List<ServiceInstance<T>> queryForInstances(String name);
}
