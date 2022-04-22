package cn.v5cn.springboot.rabbitmq.controller;

import cn.v5cn.springboot.rabbitmq.config.DelayRabbitMQConfig;
import cn.v5cn.springboot.rabbitmq.util.Constants;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZYW
 * @version 1.0
 * @date 2018-12-26 21:08
 */
@RestController
public class SendController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String send(String message) {
        rabbitTemplate.convertAndSend(Constants.VEHICLE_DRIVE_IN_EXCHANGE,"",message);
        return "success";
    }

    @GetMapping("/delay/send")
    public String delaySend(String message) {
        MessageProperties properties = new MessageProperties();

        properties.setDelay(6000);

        Message message1 = new Message(message.getBytes(), properties);

        System.out.println("发送时间：" + System.currentTimeMillis() + " ThreadID: " + Thread.currentThread().getName());
        rabbitTemplate.send(DelayRabbitMQConfig.EXCHANGE_NAME,DelayRabbitMQConfig.QUEUE_NAME,message1);
        return "success";
    }
}
