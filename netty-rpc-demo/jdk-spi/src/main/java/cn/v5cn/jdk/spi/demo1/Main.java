package cn.v5cn.jdk.spi.demo1;

import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-29 15:32
 */
public class Main {
    public static void main(String[] args) {
        List<SayHello> services = new ServiceObtain().services();
        services.forEach(item -> {
            String say = item.say("张三");
            System.out.println(say);
        });
    }
}
