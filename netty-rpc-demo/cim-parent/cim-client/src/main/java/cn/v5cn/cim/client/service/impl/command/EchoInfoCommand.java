package cn.v5cn.cim.client.service.impl.command;

import cn.v5cn.cim.client.service.EchoService;
import cn.v5cn.cim.client.service.InnerCommand;
import cn.v5cn.cim.client.service.impl.ClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author crossoverJie
 */
@Service
public class EchoInfoCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(EchoInfoCommand.class);

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String msg) {
        echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        echoService.echo("client info={}", clientInfo.get().getUserName());
        echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
