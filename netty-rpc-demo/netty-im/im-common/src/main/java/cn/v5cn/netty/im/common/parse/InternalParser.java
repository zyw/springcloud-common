package cn.v5cn.netty.im.common.parse;

import cn.v5cn.netty.im.protobuf.generate.Internal;

/**
 * 
 * @author yrw
 */
public class InternalParser extends AbstractByEnumParser<Internal.InternalMsg.MsgType, Internal.InternalMsg>  {

    public InternalParser(int size) {
        super(size);
    }

    @Override
    protected Internal.InternalMsg.MsgType getType(Internal.InternalMsg msg) {
        return msg.getMsgType();
    }
}
