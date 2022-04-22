package cn.v5cn.netty.im.transfer.start;

import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.transfer.config.TransferConfig;
import cn.v5cn.netty.im.transfer.config.TransferModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yrw
 */
public class TransferStarter {

    public static TransferConfig TRANSFER_CONFIG = new TransferConfig();
    public static TransferMqProducer producer;
    static Injector injector = Guice.createInjector(new TransferModule());

    public static void main(String[] args) {
        try {
            //parse start parameter
            TransferStarter.TRANSFER_CONFIG = parseConfig();

            //start rabbitmq server
            producer = new TransferMqProducer(TRANSFER_CONFIG.getRabbitmqHost(), TRANSFER_CONFIG.getRabbitmqPort(),
                    TRANSFER_CONFIG.getRabbitmqUsername(), TRANSFER_CONFIG.getRabbitmqPassword());

            //start transfer server
            TransferServer.startTransferServer(TRANSFER_CONFIG.getPort());
        } catch (Exception e) {
            LoggerFactory.getLogger(TransferStarter.class).error("[transfer] start failed", e);
        }
    }

    private static TransferConfig parseConfig() throws IOException {
        Properties  properties = getProperties();

        TransferConfig config = new TransferConfig();

        try {
            config.setPort(Integer.parseInt((String) properties.get("port")));
            config.setRedisHost(properties.getProperty("redis.host"));
            config.setRedisPort(Integer.parseInt(properties.getProperty("redis.port")));
            config.setRedisPassword(properties.getProperty("redis.password"));
            config.setRabbitmqHost(properties.getProperty("rabbitmq.host"));
            config.setRabbitmqUsername(properties.getProperty("rabbitmq.username"));
            config.setRabbitmqPassword(properties.getProperty("rabbitmq.password"));
            config.setRabbitmqPort(Integer.parseInt(properties.getProperty("rabbitmq.port")));
        } catch (Exception e) {
            throw new ImException("there's a parse error, check your config properties");
        }

        System.setProperty("log.path", properties.getProperty("log.path"));
        System.setProperty("log.level", properties.getProperty("log.level"));

        return config;
    }

    private static Properties getProperties() throws IOException {
        InputStream inputStream;
        final String path = System.getProperty("config");
        if (path == null) {
            throw new ImException("transfer.properties is not defined");
        } else {
            inputStream = new FileInputStream(path);
        }

        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
