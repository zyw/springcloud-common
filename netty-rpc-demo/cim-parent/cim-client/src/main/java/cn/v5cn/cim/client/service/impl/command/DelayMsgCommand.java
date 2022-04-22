package cn.v5cn.cim.client.service.impl.command;

import cn.v5cn.cim.client.service.EchoService;
import cn.v5cn.cim.client.service.InnerCommand;
import cn.v5cn.cim.client.service.MsgHandle;
import cn.v5cn.cim.common.data.construct.RingBufferWheel;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author crossoverJie
 */
@Service
public class DelayMsgCommand implements InnerCommand {

    @Autowired
    private EchoService echoService;

    @Autowired
    private MsgHandle msgHandle;

    @Autowired
    private RingBufferWheel ringBufferWheel;

    @Override
    public void process(String msg) {
        if (msg.split(" ").length <= 2) {
            echoService.echo("incorrent commond, :delay [msg] [delayTime]");
            return;
        }

        String message = msg.split(" ")[1];
        Integer delayTime = Integer.valueOf(msg.split(" ")[2]);

        RingBufferWheel.Task task = new DelayMsgJob(message);
        task.setKey(delayTime);
        ringBufferWheel.addTask(task);
        echoService.echo(EmojiParser.parseToUnicode(msg));
    }

    private class DelayMsgJob extends RingBufferWheel.Task {
        private String msg;

        public DelayMsgJob(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            msgHandle.sendMsg(msg);
        }
    }
}
