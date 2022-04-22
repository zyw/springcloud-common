package cn.v5cn.rpc.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * 对外暴露服务
 * @author ZYW
 * @version 1.0
 * @date 2020-03-05 22:18
 */
public class HelloServer {
    private Server server;

    private void start() throws IOException {
        int port = 50051;

        server = ServerBuilder.forPort(port)
                .addService(new HelloServiceImpl())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                HelloServer.this.stop();
            }
        });
    }

    /**
     * 关闭端口
     */
    private void stop() {
        if(server != null) {
            server.shutdown();
        }
    }

    /**
     * 优雅关闭
     * @throws InterruptedException
     */
    private void blockUntilShutdown() throws InterruptedException {
        if(server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final HelloServer server = new HelloServer();
        server.start();
        server.blockUntilShutdown();
    }
}
