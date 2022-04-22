package cn.v5cn.liyue.rpc.netty.server;

import cn.v5cn.liyue.rpc.api.spi.Singleton;
import cn.v5cn.liyue.rpc.netty.client.ServiceTypes;
import cn.v5cn.liyue.rpc.netty.client.stubs.RpcRequest;
import cn.v5cn.liyue.rpc.netty.serialize.SerializeSupport;
import cn.v5cn.liyue.rpc.netty.transport.RequestHandler;
import cn.v5cn.liyue.rpc.netty.transport.command.Code;
import cn.v5cn.liyue.rpc.netty.transport.command.Command;
import cn.v5cn.liyue.rpc.netty.transport.command.Header;
import cn.v5cn.liyue.rpc.netty.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 注册服务提供者和处理服务请求(任意参数和返回值)
 * @author LiYue
 * Date: 2019/9/23
 */
@Singleton
public class RpcRequestHandler2 implements RequestHandler, ServiceProviderRegistry  {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler2.class);

    // 服务名称和服务提供者这映射Map
    private Map<String/*service name*/, Object/*service provider*/> serviceProviders = new HashMap<>();

    @Override
    public <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        logger.info("Add service: {}, provider: {}.",
                serviceClass.getCanonicalName(),
                serviceProvider.getClass().getCanonicalName());
    }

    @Override
    public Command handle(Command request) {
        Header header = request.getHeader();
        // 从payload中反序列化RpcRequest
        RpcRequest rpcRequest = SerializeSupport.parse(request.getPayload());
        try {
            logger.info("service provider: {}",rpcRequest.getInterfaceName());
            // 查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if(serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                Object[] args = SerializeSupport.parse(rpcRequest.getSerializedArguments());
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), requestParameterTypes(args));
                Object result = method.invoke(serviceProvider, args);
                // 把结果封装成响应命令并返回
                return new Command(new ResponseHeader(type(),header.getVersion(),header.getRequestId()),SerializeSupport.serialize(result));
            }
            // 如果没找到，返回NO_PROVIDER错误响应。
            logger.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);
        } catch (Throwable e) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            logger.warn("Exception: ", e);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.UNKNOWN_ERROR.getCode(), e.getMessage()), new byte[0]);
        }
    }

    private Class<?>[] requestParameterTypes(Object[] args) {
        Class<?>[] paramTypes = new Class[0];
        return Arrays.stream(args).map(e -> (Class<?>)e.getClass()).collect(Collectors.toList()).toArray(paramTypes);
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }
}
