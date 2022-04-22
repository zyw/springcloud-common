package cn.v5cn.cim.client.service.impl.command;

import cn.v5cn.cim.client.service.EchoService;
import cn.v5cn.cim.client.service.InnerCommand;
import cn.v5cn.cim.client.service.MsgLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author crossoverJie
 */
@Service
public class QueryHistoryCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(QueryHistoryCommand.class);

    @Autowired
    private MsgLogger msgLogger;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String msg) {
        String[] split = msg.split(" ");
        if (split.length < 2) {
            return;
        }
        String res = msgLogger.query(split[1]);
        echoService.echo(res);
    }
}
