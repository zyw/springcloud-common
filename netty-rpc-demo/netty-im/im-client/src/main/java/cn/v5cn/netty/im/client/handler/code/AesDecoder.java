package cn.v5cn.netty.im.client.handler.code;

import cn.v5cn.netty.im.client.context.UserContext;
import cn.v5cn.netty.im.common.domain.po.Relation;
import cn.v5cn.netty.im.common.util.Encryption;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class AesDecoder extends MessageToMessageDecoder<Message> {

    private UserContext userContext;

    public AesDecoder(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        if(msg instanceof Chat.ChatMsg) {
            Chat.ChatMsg cm = (Chat.ChatMsg)msg;
            final Relation relation = userContext.getRelation(cm.getFromId(), cm.getDestId());
            final String[] keys = relation.getEncryptKey().split("\\|");
            final byte[] decodeBody = Encryption.decrypt(keys[0], keys[1], cm.getMsgBody().toByteArray());
            final Chat.ChatMsg decodeMsg = Chat.ChatMsg.newBuilder().mergeFrom(cm).setMsgBody(ByteString.copyFrom(decodeBody)).build();

            out.add(decodeBody);
        } else {
            out.add(msg);
        }
    }
}
