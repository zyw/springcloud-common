package cn.v5cn.netty.ws.pb.core.parser;

import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;

/**
 * @author ZYW
 */
public class InternalEnumParser extends AbstractByEnumParser<InternalMsg.MsgType,InternalMsg> {

    public InternalEnumParser(int size) {
        super(size);
    }

    @Override
    protected InternalMsg.MsgType getType(InternalMsg msg) {
        return msg.getMsgType();
    }

}
