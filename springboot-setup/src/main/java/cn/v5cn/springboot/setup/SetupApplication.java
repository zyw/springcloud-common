package cn.v5cn.springboot.setup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SetupApplication {
    public static void main(String[] args) {
        SpringApplication.run(SetupApplication.class,args);
    }

    @PostConstruct
    public void init() {
        System.out.println("main----------------------------------------------");
    }
}
