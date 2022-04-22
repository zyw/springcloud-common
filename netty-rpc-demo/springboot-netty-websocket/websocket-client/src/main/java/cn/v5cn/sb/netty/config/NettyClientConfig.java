package cn.v5cn.sb.netty.config;

import cn.v5cn.sb.netty.common.dispacher.MessageDispatcher;
import cn.v5cn.sb.netty.common.dispacher.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:22 下午
 */
@Configuration
public class NettyClientConfig {

    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }
}
