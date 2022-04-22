package cn.v5cn.liyue.rpc.netty.client;

import cn.v5cn.liyue.rpc.netty.client.stubs.RpcRequest;
import cn.v5cn.liyue.rpc.netty.serialize.SerializeSupport;
import cn.v5cn.liyue.rpc.netty.transport.Transport;
import cn.v5cn.liyue.rpc.netty.transport.command.Code;
import cn.v5cn.liyue.rpc.netty.transport.command.Command;
import cn.v5cn.liyue.rpc.netty.transport.command.Header;
import cn.v5cn.liyue.rpc.netty.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutionException;

/**
 * JDK动态代理实现客户端桩
 */
public class JDKDynamicStubFactory implements StubFactory, InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(JDKDynamicStubFactory.class);

    protected Transport transport;
    private String interfaceName;
    private String methodName;

    @Override
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        this.transport = transport;
        this.interfaceName = serviceClass.getCanonicalName();
        this.methodName = serviceClass.getMethods()[0].getName();
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest(interfaceName,methodName, SerializeSupport.serialize(args));
        logger.info("RpcRequest: {}",request.toString());
        Header header = new Header(ServiceTypes.TYPE_RPC_REQUEST,1, RequestIdSupport.next());
        byte[] payload = SerializeSupport.serialize(request);
        Command command = new Command(header, payload);
        try {
            Command responseCommand = transport.send(command).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if(responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return SerializeSupport.parse(responseCommand.getPayload());
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
