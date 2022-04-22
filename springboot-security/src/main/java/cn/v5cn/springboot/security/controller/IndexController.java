package cn.v5cn.springboot.security.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZYW
 * @version 1.0
 * @date 2019-09-10 21:03
 */
@RestController
public class IndexController {

    @GetMapping("/index")
    public String index(){
        return "Hello World!";
    }

    @GetMapping("/json")
    public Object json(){
        List<Map<String,String>> result = new ArrayList<>(2);
        Map<String,String> node1 = new HashMap<>();
        node1.put("Address","192.168.1.5");
        node1.put("ServicePort","9991");
        result.add(node1);

        Map<String,String> node2 = new HashMap<>();
        node2.put("Address","192.168.1.5");
        node2.put("ServicePort","9992");
        result.add(node2);

        Map<String,String> node3 = new HashMap<>();
        node3.put("Address","192.168.1.15");
        node3.put("ServicePort","8080");
        result.add(node3);

        return result;
    }
}
