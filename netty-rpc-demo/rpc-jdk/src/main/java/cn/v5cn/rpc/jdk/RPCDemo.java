package cn.v5cn.rpc.jdk;

import java.net.InetSocketAddress;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-27 17:18
 */
public class RPCDemo {
    public static void main(String[] args) {
        //启动服务端
        new Thread(() -> {
            try {
                RPCServer.exporter("localhost",9999);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //启动客户端
        RPCClient<SayHello> importer = new RPCClient<>();
        //这里需要传SayHellImpl.class作为参数，在其他RPC框架中，客户的是不知道这个类的存在的。
        SayHello sayHello = importer.importer(SayHello.class, new InetSocketAddress("localhost", 9999));
        System.out.println(sayHello.say("张三"));
    }
}
