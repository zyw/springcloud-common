package cn.v5cn.sb.netty.config;

import cn.v5cn.sb.netty.common.dispacher.MessageDispatcher;
import cn.v5cn.sb.netty.common.dispacher.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyServerConfig {

    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }

    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }
}
