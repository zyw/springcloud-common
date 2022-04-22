package cn.v5cn.elasticsearch751.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class ElasticsearchRestClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchRestClientConfig.class);

    private static final int ADDRESS_LENGTH = 2;
    private static final String HTTP_SCHEME = "http";

    @Value("${spring.elasticsearch.ip}")
    private String[] ipAddress;

    @Bean
    public RestClientBuilder restClientBuilder(){
        HttpHost[] hosts = Arrays.stream(ipAddress)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);

        LOGGER.info("hosts:{}",Arrays.toString(hosts));

        return RestClient.builder(hosts);
    }

    @Bean
    @DependsOn("restClientBuilder")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

    private HttpHost makeHttpHost(String s) {
        assert StringUtils.isEmpty(s);
        String[] address = s.split(":");
        if(address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip,port,HTTP_SCHEME);
        } else {
            return null;
        }
    }

}
