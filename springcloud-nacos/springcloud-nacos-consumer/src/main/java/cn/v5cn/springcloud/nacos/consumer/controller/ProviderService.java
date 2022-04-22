package cn.v5cn.springcloud.nacos.consumer.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "nacos-provider")
public interface ProviderService {

    @GetMapping("/provider/list")
    String list();
}
