package cn.v5cn.liyue.rpc.netty;

import cn.v5cn.liyue.rpc.api.RpcAccessPoint;
import cn.v5cn.liyue.rpc.api.spi.ServiceSupport;
import cn.v5cn.liyue.rpc.netty.client.StubFactory;
import cn.v5cn.liyue.rpc.netty.server.ServiceProviderRegistry;
import cn.v5cn.liyue.rpc.netty.transport.RequestHandlerRegistry;
import cn.v5cn.liyue.rpc.netty.transport.Transport;
import cn.v5cn.liyue.rpc.netty.transport.TransportClient;
import cn.v5cn.liyue.rpc.netty.transport.TransportServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author LiYue
 * Date: 2019/9/20
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {

    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);
    private TransportServer server = null;
    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private final Map<URI, Transport> clientMap= new ConcurrentHashMap<>();
    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);
    private ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        final Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport,serviceClass);
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass,service);
        return uri;
    }

    @Override
    public Closeable startServer() throws Exception {
        if(null == server) {
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(),port);
        }
        return () -> {
            if(null != server) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if(null != server) {
            server.stop();
        }
        client.close();
    }

    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(),uri.getPort()),30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
