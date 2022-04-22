package cn.v5cn.sofarpc.demo.impl;

import cn.v5cn.sofarpc.demo.HelloService;

/**
 * Quick Start demo implement
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        System.out.println("Server receive: " + name);
        return "hello " + name + " ！";
    }
}
