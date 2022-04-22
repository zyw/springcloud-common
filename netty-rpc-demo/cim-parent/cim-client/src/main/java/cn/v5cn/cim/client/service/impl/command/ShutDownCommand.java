package cn.v5cn.cim.client.service.impl.command;

import cn.v5cn.cim.client.client.CIMClient;
import cn.v5cn.cim.client.service.*;
import cn.v5cn.cim.common.data.construct.RingBufferWheel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author crossoverJie
 */
@Service
public class ShutDownCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(ShutDownCommand.class);

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private CIMClient cimClient;

    @Autowired
    private MsgLogger msgLogger;

    @Resource(name = "callBackThreadPool")
    private ThreadPoolExecutor callBackExecutor;

    @Autowired
    private EchoService echoService;

    @Autowired
    private ShutDownMsg shutDownMsg;

    @Autowired
    private RingBufferWheel ringBufferWheel;

    @Override
    public void process(String msg) {
        echoService.echo("cim client closing...");
        shutDownMsg.shutdown();
        routeRequest.offLine();
        msgLogger.stop();
        callBackExecutor.shutdown();
        ringBufferWheel.stop(false);
        try {
            while (!callBackExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                echoService.echo("thread pool closing");
            }
            cimClient.close();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
        echoService.echo("cim close success!");
        System.exit(0);
    }
}
