package cn.v5cn.web.pc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zyw
 */
@SpringBootApplication(scanBasePackages = {"cn.v5cn.web.pc","cn.v5cn.web.common"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
