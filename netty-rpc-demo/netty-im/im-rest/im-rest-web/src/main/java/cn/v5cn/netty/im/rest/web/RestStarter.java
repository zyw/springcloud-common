package cn.v5cn.netty.im.rest.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author yrw
 */
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"cn.v5cn.netty.im.rest"})
public class RestStarter {
    public static void main(String[] args) {
        SpringApplication.run(RestStarter.class, args);
    }
}
