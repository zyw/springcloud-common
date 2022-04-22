package cn.v5cn.springcloud.rocketmq.service;

import cn.v5cn.springcloud.rocketmq.util.Constants;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = Constants.SPRING_TOPIC,consumerGroup = Constants.SPRING_TOPIC_CONSUMER + "02")
public class StringConsumerService3 implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("接收到消息时间3：" + System.currentTimeMillis());
        System.out.println(s);
    }
}
