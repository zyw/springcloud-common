package cn.v5cn.web.pc.controller;

import cn.v5cn.web.common.rwv.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyw
 */
@RestController
public class IndexController {

    @ResponseResult
    @GetMapping("/index")
    public Object index() {
        Map<String,Object> result = new HashMap<>();
        result.put("name","zhangsan");
        result.put("age", 12);
        return result;
    }

    @ResponseResult
    @GetMapping("/index2")
    public Object index2() {
        throw new RuntimeException("人为异常");
    }
}
