package cn.v5cn.springboot.rabbitmq.receive;

import cn.v5cn.springboot.rabbitmq.config.DelayRabbitMQConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author ZYW
 * @version 1.0
 * @date 2018/12/27 12:19
 */
@Component
public class ReceiveMessage {

    @RabbitListener(queues = {DelayRabbitMQConfig.QUEUE_NAME})
    public void receiveDelay(Message message, Channel channel) throws IOException {
        // 服务器将传递的最大消息数，如果无限制，则为0
        channel.basicQos(1);

        System.out.println("接收时间：" + System.currentTimeMillis() + " ThreadID: " + Thread.currentThread().getName());
        System.out.println("接收时间：" + new String(message.getBody()));
        //确认消息已消费
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }

}
