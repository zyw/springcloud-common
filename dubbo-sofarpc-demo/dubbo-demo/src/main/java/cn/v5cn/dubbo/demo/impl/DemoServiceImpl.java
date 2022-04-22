package cn.v5cn.dubbo.demo.impl;

import cn.v5cn.dubbo.demo.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
