package cn.v5cn.cim.client;

import cn.v5cn.cim.client.scanner.Scan;
import cn.v5cn.cim.client.service.impl.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

/**
 * @author crossoverJie
 */
public class CIMClientApplication implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(CIMClientApplication.class);

    @Autowired
    private ClientInfo clientInfo;

    @Override
    public void run(String... args) throws Exception {
        Scan scan = new Scan();
        Thread thread = new Thread(scan);
        thread.setName("scan-thread");
        thread.start();
        clientInfo.saveStartDate();
    }

    public static void main(String[] args) {
        SpringApplication.run(CIMClientApplication.class, args);
        LOGGER.info("启动 Client 服务成功");
    }
}
