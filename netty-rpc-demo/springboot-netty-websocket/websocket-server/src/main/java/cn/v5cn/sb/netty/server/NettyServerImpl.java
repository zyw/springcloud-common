package cn.v5cn.sb.netty.server;

import cn.v5cn.sb.netty.tcp.NettyTCPServer;
import cn.v5cn.sb.netty.websocket.NettyWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class NettyServerImpl implements NettyServer {

    @Value("${netty.server-choose}")
    private Integer serverChoose = 1;

    @Autowired
    private NettyTCPServer tcpServer;

    @Autowired
    private NettyWebSocketServer webSocketServer;

    @PostConstruct
    @Override
    public void start() throws InterruptedException {
        if(serverChoose == 1) {
            tcpServer.start();
            return;
        }
        if(serverChoose == 2) {
            webSocketServer.start();
            return;
        }
        if (serverChoose == 3) {
            tcpServer.start();
            webSocketServer.start();
        }
    }

    @PreDestroy
    @Override
    public void shutdown() {
        if(serverChoose == 1) {
            tcpServer.shutdown();
            return;
        }
        if(serverChoose == 2) {
            webSocketServer.shutdown();
            return;
        }
        if (serverChoose == 3) {
            tcpServer.shutdown();
            webSocketServer.shutdown();
        }
    }
}
