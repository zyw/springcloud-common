package cn.v5cn.netty.im.common.parse;

import cn.v5cn.netty.im.protobuf.generate.Ack;

/**
 * 实现AbstractByEnumParser抽象类
 * @author yrw
 */
public class AckParser extends AbstractByEnumParser<Ack.AckMsg.MsgType, Ack.AckMsg> {

    public AckParser(int size) {
        super(size);
    }

    @Override
    protected Ack.AckMsg.MsgType getType(Ack.AckMsg msg) {
        return msg.getMsgType();
    }
}
