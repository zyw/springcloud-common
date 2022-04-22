package cn.v5cn.netty.ws.pb.core.parser;

import cn.v5cn.netty.ws.pb.core.entity.AckMsg;

/**
 * @author ZYW
 */
public class AckEnumParser extends AbstractByEnumParser<AckMsg.MsgType, AckMsg> {

    public AckEnumParser(int size) {
        super(size);
    }

    @Override
    protected AckMsg.MsgType getType(AckMsg msg) {
        return msg.getMsgType();
    }
}
