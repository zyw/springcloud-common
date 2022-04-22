package cn.v5cn.liyue.rpc.api;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/**
 * 注册中心
 * @author LiYue
 * Date: 2019/9/20
 */
public interface NameService {
    /**
     * 所有支持的协议
     * @return 返回支持的协议集合
     */
    Collection<String> supportedSchemes();

    /**
     * 链接注册中心
     * @param nameServiceUri 支持中心地址
     */
    void connect(URI nameServiceUri);

    /**
     * 注册服务
     * @param serviceName 服务名称
     * @param uri 服务地址
     * @throws IOException
     */
    void registerService(String serviceName,URI uri) throws IOException;

    /**
     * 查询服务地址
     * @param serviceName 服务名称
     * @return 服务地址
     * @throws IOException
     */
    URI lookupService(String serviceName) throws IOException;
}
