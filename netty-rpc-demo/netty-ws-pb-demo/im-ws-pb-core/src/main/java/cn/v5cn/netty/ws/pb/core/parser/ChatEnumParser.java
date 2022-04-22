package cn.v5cn.netty.ws.pb.core.parser;

import cn.v5cn.netty.ws.pb.core.entity.ChatMsg;

/**
 * @author zyw
 */
public class ChatEnumParser extends AbstractByEnumParser<ChatMsg.DestType,ChatMsg> {

    public ChatEnumParser(int size) {
        super(size);
    }

    @Override
    protected ChatMsg.DestType getType(ChatMsg msg) {
        return msg.getDestType();
    }
}
