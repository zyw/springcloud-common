package cn.v5cn.rpc.lagou.proxy;

import cn.v5cn.rpc.lagou.Constants;
import cn.v5cn.rpc.lagou.protocol.Header;
import cn.v5cn.rpc.lagou.protocol.Message;
import cn.v5cn.rpc.lagou.protocol.Request;
import cn.v5cn.rpc.lagou.registry.Registry;
import cn.v5cn.rpc.lagou.registry.ServerInfo;
import cn.v5cn.rpc.lagou.transport.Connection;
import cn.v5cn.rpc.lagou.transport.DemoRpcClient;
import cn.v5cn.rpc.lagou.transport.NettyResponseFuture;
import io.netty.channel.ChannelFuture;
import org.apache.curator.x.discovery.ServiceInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class DemoRpcProxy implements InvocationHandler {

    /**
     * 需要代理的服务(接口)名称
     */
    private String serviceName;

    /**
     * 用于与Zookeeper交互，其中自带缓存
     */
    private Registry<ServerInfo> registry;

    public DemoRpcProxy(String serviceName, Registry<ServerInfo> registry) {
        this.serviceName = serviceName;
        this.registry = registry;
    }

    public static <T> T newInstance(Class<T> clazz,Registry<ServerInfo> registry) {
        // 创建代理对象
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{clazz},
                new DemoRpcProxy(clazz.getName(),registry));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 从Zookeeper缓存中获取可用的Server地址,并随机从中选择一个
        final List<ServiceInstance<ServerInfo>> serviceInstances = registry.queryForInstances(serviceName);
        final ServiceInstance<ServerInfo> serviceInstance = serviceInstances.get(ThreadLocalRandom.current().nextInt(serviceInstances.size()));
        // 创建请求消息，然后调用remoteCall()方法请求上面选定的Server端
        final String methodName = method.getName();
        Header header = new Header(Constants.MAGIC,Constants.VERSION1_0, Integer.valueOf("10000000",2).byteValue(),0L,0);
        Message<Request> message = new Message(header,
                new Request(serviceName, methodName,null, args));
        return remoteCall(serviceInstance.getPayload(), message);
    }

    private Object remoteCall(ServerInfo serverInfo, Message<Request> message) throws Exception {
        if (serverInfo == null) {
            throw new RuntimeException("get available server error");
        }
        // 创建DemoRpcClient连接指定的Server端
        DemoRpcClient demoRpcClient = new DemoRpcClient(
                serverInfo.getHost(), serverInfo.getPort());
        ChannelFuture channelFuture = demoRpcClient.connect()
                .awaitUninterruptibly();
        // 创建对应的Connection对象，并发送请求
        Connection connection = new Connection(channelFuture, true);
        NettyResponseFuture responseFuture =
                connection.request(message, Constants.DEFAULT_TIMEOUT);
        // 等待请求对应的响应
        return responseFuture.getPromise().get(
                Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

}
