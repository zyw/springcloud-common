package cn.v5cn.springboot.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 延时队列的创建
 * </p>
 *
 * @author ZYW
 * @version 1.0
 * @date 2018/12/27 11:39
 */
@Configuration
public class DelayRabbitMQConfig {

    public static final String QUEUE_NAME = "delay-queue";

    public static final String EXCHANGE_NAME = "delay-exchange";

    public static final String X_DELAYED_MESSAGE = "x-delayed-message";

    public static final String X_DELAYED_TYPE = "x-delayed-type";

    @Bean("delayExchange")
    public Exchange delayExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME)
                .delayed()
                .durable(true)
                .withArgument(X_DELAYED_TYPE, "direct")
                .build();
    }

    @Bean("delayQueue")
    public Queue delayQueue() {
        return QueueBuilder
                .durable(QUEUE_NAME)
                .build();
    }

    @Bean("delayExchangeQueueBinding")
    public Binding delayExchangeQueueBinding(Exchange delayExchange, Queue delayQueue) {
        return BindingBuilder
                .bind(delayQueue)
                .to(delayExchange)
                .with(QUEUE_NAME).noargs();
    }
}
