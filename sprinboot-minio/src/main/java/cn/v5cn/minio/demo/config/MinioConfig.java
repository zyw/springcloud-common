package cn.v5cn.minio.demo.config;

import cn.v5cn.minio.demo.exception.MyMinioException;
import com.google.common.base.Splitter;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-11 15:41
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioConfig.class);

    private String endpoints;

    private String accessKey;

    private String secretKey;

    @Bean
    public MinioClientList minioClients() {
        List<String> endpointList = Splitter.on(",").trimResults().splitToList(endpoints);
        if(CollectionUtils.isEmpty(endpointList)) {
            throw new MyMinioException("Minio连接地址为空");
        }
        List<MinioClient> clients = endpointList.stream().map(item -> {
            try {
                return new MinioClient(item,accessKey,secretKey);
            } catch (InvalidEndpointException | InvalidPortException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(),e);
                throw new MyMinioException("创建MinioClient异常");
            }
        }).collect(Collectors.toList());
        return new MinioClientList(clients);
    }

    public void setEndpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
