package cn.v5cn.netty.im.transfer.config;

/**
 * transfer 配置文件，配置Netty的端口，redis的连接。
 * rabbitMQ的配置信息。
 * @author yrw
 */
public class TransferConfig {
    private Integer port;

    private String redisHost;

    private Integer redisPort;

    private String redisPassword;

    private String rabbitmqHost;

    private Integer rabbitmqPort;

    private String rabbitmqUsername;

    private String rabbitmqPassword;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public String getRabbitmqHost() {
        return rabbitmqHost;
    }

    public void setRabbitmqHost(String rabbitmqHost) {
        this.rabbitmqHost = rabbitmqHost;
    }

    public Integer getRabbitmqPort() {
        return rabbitmqPort;
    }

    public void setRabbitmqPort(Integer rabbitmqPort) {
        this.rabbitmqPort = rabbitmqPort;
    }

    public String getRabbitmqUsername() {
        return rabbitmqUsername;
    }

    public void setRabbitmqUsername(String rabbitmqUsername) {
        this.rabbitmqUsername = rabbitmqUsername;
    }

    public String getRabbitmqPassword() {
        return rabbitmqPassword;
    }

    public void setRabbitmqPassword(String rabbitmqPassword) {
        this.rabbitmqPassword = rabbitmqPassword;
    }
}
