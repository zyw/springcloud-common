package cn.v5cn.rpc.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-03 16:45
 */
public class HelloClient {

    private final ManagedChannel channel;
    private final HelloProtoGrpc.HelloProtoBlockingStub blockingStub;

    /**
     * 构建Channel连接
     * @param host 服务地址
     * @param port 服务端口
     */
    public HelloClient(String host,int port) {
        this(ManagedChannelBuilder.forAddress(host,port).usePlaintext().build());
    }

    /**
     * 构建Stub用于发请求
     * @param channel
     */
    HelloClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = HelloProtoGrpc.newBlockingStub(channel);
    }

    /**
     * 调用完手动关闭
     * @throws InterruptedException
     */
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * 发送RPC请求
     * @param name 参数
     */
    public void say(String name) {
        //构建入参对象
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply reply;
        try {
            reply = blockingStub.say(request);
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(reply);
    }

    public static void main(String[] args) throws InterruptedException {
        HelloClient client = new HelloClient("localhost",50051);
        try {
            client.say("zhangsan");
        } finally {
            client.shutdown();
        }
    }

}
