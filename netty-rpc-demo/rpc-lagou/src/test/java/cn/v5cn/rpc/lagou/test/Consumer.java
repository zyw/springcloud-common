package cn.v5cn.rpc.lagou.test;

import cn.v5cn.rpc.lagou.proxy.DemoRpcProxy;
import cn.v5cn.rpc.lagou.registry.ServerInfo;
import cn.v5cn.rpc.lagou.registry.ZookeeperRegistry;

public class Consumer {
    public static void main(String[] args) throws Exception {
        // 创建ZookeeperRegistr对象
        ZookeeperRegistry<ServerInfo> discovery = new ZookeeperRegistry<>();
        discovery.start();
        // 创建代理对象，通过代理调用远端Server
        DemoService demoService = DemoRpcProxy.newInstance(DemoService.class, discovery);
        // 调用sayHello()方法，并输出结果
        String result = demoService.sayHello("hello");
        System.out.println(result);
    }
}
