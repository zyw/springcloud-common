package cn.v5cn.actuator.demo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-15 16:38
 */
@SpringBootApplication
@EnableAdminServer
public class ActuatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class,args);
    }
}
