package cn.v5cn.netty.im.client;

import cn.v5cn.netty.im.client.api.ChatApi;
import cn.v5cn.netty.im.client.api.ClientMsgListener;
import cn.v5cn.netty.im.client.api.UserApi;
import cn.v5cn.netty.im.client.context.UserContext;
import cn.v5cn.netty.im.client.handler.ClientConnectorHandler;
import cn.v5cn.netty.im.client.handler.code.AesDecoder;
import cn.v5cn.netty.im.client.handler.code.AesEncoder;
import cn.v5cn.netty.im.client.service.ClientRestService;
import cn.v5cn.netty.im.common.code.MsgDecoder;
import cn.v5cn.netty.im.common.code.MsgEncoder;
import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.common.util.IdWorker;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ImClient {
    private static Logger logger = LoggerFactory.getLogger(ImClient.class);

    public Injector injector;
    public final String connectionId = IdWorker.uuid();

    private String connectorHost;
    private Integer connectorPort;
    private ClientMsgListener clientMsgListener;
    private UserContext userContext;
    private ClientConnectorHandler handler;

    public ImClient(String connectorHost, Integer connectorPort, String restUrl) {
        this(connectorHost, connectorPort, restUrl, null);
    }

    public ImClient(String connectorHost, Integer connectorPort, String restUrl, ClientMsgListener clientMsgListener) {
        assert connectorHost != null;
        assert connectorPort != null;
        assert restUrl != null;

        this.connectorHost = connectorHost;
        this.connectorPort = connectorPort;
        this.clientMsgListener = clientMsgListener;

        ClientRestServiceProvider.REST_URL = restUrl;
        this.injector = Guice.createInjector(new ClientModule());
    }

    public void start() {
        assert clientMsgListener != null;

        userContext = injector.getInstance(UserContext.class);
        handler = new ClientConnectorHandler(clientMsgListener);
        userContext.setClientConnectorHandler(handler);

        startImClient(handler);
    }

    public void startImClient(ClientConnectorHandler handler) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture f = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        final ChannelPipeline pipeline = ch.pipeline();

                        // out
                        pipeline.addLast("MsgEncoder", new MsgEncoder())
                                .addLast("AesEncoder", new AesEncoder(userContext))

                                // in
                                .addLast("MsgDecoder", new MsgDecoder())
                                .addLast("AesDecoder", new AesDecoder(userContext))
                                .addLast("ClientConnectorHander", handler);
                    }
                }).connect(connectorHost, connectorPort)
                .addListener((ChannelFutureListener) future -> {
                    if(future.isSuccess()) {
                        logger.info("ImClient connect to connector successfully");
                    } else {
                        throw new ImException("[client] connect to connector failed! connector url: "
                                + connectorHost + ":" + connectorPort);
                    }
                });
        try {
            f.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ImException("[client] connect to connector failed!");
        }
    }

    public String getConnectorHost() {
        return connectorHost;
    }

    public Integer getConnectorPort() {
        return connectorPort;
    }

    public ClientMsgListener getClientMsgListener() {
        return clientMsgListener;
    }

    public void setClientMsgListener(ClientMsgListener clientMsgListener) {
        this.clientMsgListener = clientMsgListener;
    }

    public ChatApi chatApi() {
        return new ChatApi(connectionId, userContext, handler);
    }

    public UserApi userApi() {
        return new UserApi(injector.getInstance(ClientRestService.class), userContext, handler);
    }
}
