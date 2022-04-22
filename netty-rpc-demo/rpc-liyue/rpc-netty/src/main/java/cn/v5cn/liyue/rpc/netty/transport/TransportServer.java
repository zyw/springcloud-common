package cn.v5cn.liyue.rpc.netty.transport;

/**
 * @author LiYue
 * Date: 2019/9/25
 */
public interface TransportServer {
    /**
     * 启动后端RPC服务
     * @param requestHandlerRegistry 请求处理器
     * @param port 服务端口
     * @throws Exception
     */
    void start(RequestHandlerRegistry requestHandlerRegistry,int port) throws Exception;

    /**
     * 停止RPC服务
     */
    void stop();
}
