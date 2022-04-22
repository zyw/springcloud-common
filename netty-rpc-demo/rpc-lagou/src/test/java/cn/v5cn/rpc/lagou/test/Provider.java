package cn.v5cn.rpc.lagou.test;

import cn.v5cn.rpc.lagou.registry.ServerInfo;
import cn.v5cn.rpc.lagou.registry.ZookeeperRegistry;
import cn.v5cn.rpc.lagou.transport.BeanManager;
import cn.v5cn.rpc.lagou.transport.DemoRpcServer;
import org.apache.curator.x.discovery.ServiceInstance;

public class Provider {
    public static void main(String[] args) throws Exception {
        // 创建DemoServiceImpl，并注册到BeanManager中
        BeanManager.registerBean("demoService", new DemoServiceImpl());
        // 创建ZookeeperRegistry，并将Provider的地址信息封装成ServerInfo
        // 对象注册到Zookeeper
        ZookeeperRegistry<ServerInfo> discovery = new ZookeeperRegistry<>();
        discovery.start();
        ServerInfo serverInfo = new ServerInfo("127.0.0.1", 20880,"");
        System.out.println(DemoService.class.getCanonicalName()+"------------------------------------");
        discovery.registryService(ServiceInstance.<ServerInfo>builder().name(DemoService.class.getCanonicalName()).payload(serverInfo).build());
        // 启动DemoRpcServer，等待Client的请求
        DemoRpcServer rpcServer = new DemoRpcServer(20880);
        rpcServer.start();
    }
}
