package cn.v5cn.springboot.rabbitmq.config;

import cn.v5cn.springboot.rabbitmq.util.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author ZYW
 * @version 1.0
 * @date 2018/12/26 17:26
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 设置消息转换
     * @return
     */
    /*@Bean
    @Primary
    public RabbitTemplate rabbitTemplate(RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }*/

    @Bean("driveInExchange")
    public FanoutExchange driveInExchange() {
        return (FanoutExchange)ExchangeBuilder.fanoutExchange(Constants.VEHICLE_DRIVE_IN_EXCHANGE).durable(true).build();
    }

    /**
     * 声明一个车辆驶入队列 支持持久化.并绑定
     *
     * @return the queue
     */
    @Bean("driveInQueue")
    public Queue driveInQueue() {
        return QueueBuilder.durable(Constants.VEHICLE_DRIVE_IN_QUEUE + "num01").withArgument("x-queue-mode","lazy").build();
    }

    @Bean("driveInQueue2")
    public Queue driveInQueue2() {
        return QueueBuilder.durable(Constants.VEHICLE_DRIVE_IN_QUEUE + "num02").withArgument("x-queue-mode","lazy").build();
    }

    @Bean("driveInExchangeQueueBinding")
    public Binding driveInExchangeQueueBinding(FanoutExchange driveInExchange,Queue driveInQueue) {
        return BindingBuilder.bind(driveInQueue).to(driveInExchange);
    }

    @Bean("driveInExchangeQueueBinding2")
    public Binding driveInExchangeQueueBinding2(FanoutExchange driveInExchange,Queue driveInQueue2) {
        return BindingBuilder.bind(driveInQueue2).to(driveInExchange);
    }


}
