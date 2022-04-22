package cn.v5cn.springcloud.nacos.config.controller;

import cn.v5cn.springcloud.nacos.config.config.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhuyanwei
 */
@RestController
@RefreshScope
public class IndexController {

    @Autowired
    private TestConfig testConfig;

    @GetMapping("/nacos/config")
    public String index(){
        return "nacos: " + testConfig.getPath() + "######" + testConfig.getKey();
    }
}
