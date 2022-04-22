package cn.v5cn.cim.server.config;

import cn.v5cn.cim.server.endpoint.CustomEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author crossoverJie
 */
@Configuration
public class EndPointConfig {

    @Value("${monitor.channel.map.key}")
    private String channelMap;

    @Bean
    public CustomEndpoint buildEndPoint() {
        return new CustomEndpoint(channelMap);
    }
}
