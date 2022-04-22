package cn.v5cn.springcloud.rocketmq.controller;

import cn.v5cn.springcloud.rocketmq.util.Constants;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rocket")
public class RocketMQController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/send")
    public String sendMQ() throws UnsupportedEncodingException {
        /*SendResult sendResult = rocketMQTemplate.syncSend(Constants.SPRING_TOPIC, "Hello, World!");
        System.out.printf("string-topic syncSend1 sendResult=%s %n", sendResult);

        // Send string with spring Message
        sendResult = rocketMQTemplate.syncSend(Constants.SPRING_TOPIC, MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
        System.out.printf("string-topic syncSend2 sendResult=%s %n", sendResult);*/

        //org.apache.rocketmq.common.message.Message message1 = new org.apache.rocketmq.common.message.Message(Constants.SPRING_TOPIC,"HTLLOOOOOOO".getBytes());
        /**
         * RocketMQ延时队列，设置级别
         * messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
         * 1=1s,2=5s,3=10s,4=30s 以此类推
         */
        //message1.setDelayTimeLevel(4);
       // Message message2 = RocketMQUtil.convertToSpringMsg(message1);
//        Map<String,String> propers = new HashMap<>();
//        propers.put(MessageConst.PROPERTY_DELAY_TIME_LEVEL, String.valueOf(3));
        Message<String> message2 = MessageBuilder.withPayload("Helloooooooooo").setHeader("WAIT_STORE_MSG_OK",true)
                //.setHeader(RocketMQMessageConst.DELAY_TIME_LEVEL, String.valueOf(5))
                .build();

        SendResult sendResult = rocketMQTemplate.syncSend(Constants.SPRING_TOPIC,message2);
        System.out.printf("string-topic syncSend1 sendResult=%s %n", sendResult);
        System.out.println("发送时间：" + System.currentTimeMillis());
        // Send user-defined object
        /*rocketMQTemplate.asyncSend(orderPaidTopic, new OrderPaidEvent("T_001", new BigDecimal("88.00")), new SendCallback() {
            public void onSuccess(SendResult var1) {
                System.out.printf("async onSucess SendResult=%s %n", var1);
            }

            public void onException(Throwable var1) {
                System.out.printf("async onException Throwable=%s %n", var1);
            }

        });*/

        // Send message with special tag
        /*rocketMQTemplate.convertAndSend(msgExtTopic + ":tag0", "I'm from tag0");  // tag0 will not be consumer-selected
        rocketMQTemplate.convertAndSend(msgExtTopic + ":tag1", "I'm from tag1");*/

        return "success";
    }
}
