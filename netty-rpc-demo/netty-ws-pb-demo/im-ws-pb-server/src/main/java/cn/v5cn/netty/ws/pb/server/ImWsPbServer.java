package cn.v5cn.netty.ws.pb.server;

import cn.v5cn.netty.ws.pb.server.config.ImServerModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author zyw
 */
public class ImWsPbServer {

    public static Injector injector = Guice.createInjector(new ImServerModule());

    public static void main(String[] args) {
        NettyStart.start(8888);
    }
}
