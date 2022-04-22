package cn.v5cn.jdk.spi.demo1.impl;

import cn.v5cn.jdk.spi.demo1.SayHello;

/**
 * 服务接口实现类，通过跟服务接口不在一个jar文件中
 * @author ZYW
 * @version 1.0
 * @date 2020-02-29 15:29
 */
public class WelcomeSayHello implements SayHello {
    @Override
    public String say(String name) {
        return "Welcome " + name;
    }
}
