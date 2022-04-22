package cn.v5cn.rpc.lagou.test;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String param) {
        return "hello:" + param;
    }
}
