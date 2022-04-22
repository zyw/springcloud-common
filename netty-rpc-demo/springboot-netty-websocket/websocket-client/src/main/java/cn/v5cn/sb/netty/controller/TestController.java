package cn.v5cn.sb.netty.controller;

import cn.v5cn.sb.netty.client.NettyClient;
import cn.v5cn.sb.netty.common.codec.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:22 下午
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NettyClient nettyClient;

    @PostMapping("/mock")
    public String mock(String type, String message) {
        logger.info("[type][{}], [message][{}]", type, message);
        // 创建 Invocation 对象
        Invocation invocation = new Invocation(type, message);
        // 发送消息
        nettyClient.send(invocation);
        return "success";
    }
}
