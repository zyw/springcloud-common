package cn.v5cn.netty.im.common.parse;

import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.protobuf.constant.MsgTypeEnum;
import cn.v5cn.netty.im.protobuf.generate.Ack;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import cn.v5cn.netty.im.protobuf.generate.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yrw
 */
public class ParseService {

    private final Map<MsgTypeEnum, Parse> parseFunctionMap;

    public ParseService() {
        parseFunctionMap = new HashMap<>(MsgTypeEnum.values().length);

        // Message中的parseFrom通过lambda表达式::变成Parse函数式接口对象。
        parseFunctionMap.put(MsgTypeEnum.CHAT, Chat.ChatMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.ACK, Ack.AckMsg::parseFrom);
        parseFunctionMap.put(MsgTypeEnum.INTERNAL, Internal.InternalMsg::parseFrom);
    }

    public Message getMsgByCode(int code, byte[] bytes) throws InvalidProtocolBufferException {
        MsgTypeEnum msgType = MsgTypeEnum.getByCode(code);
        Parse parse = parseFunctionMap.get(msgType);
        if(parse == null) {
            throw new ImException("[msg parse], no proper parse function, msgType: " + msgType.name());
        }
        return parse.process(bytes);
    }

    /**
     * 定义一个函数式接口，接口中的方法签名符合，各个Message中的parseFrom的方法签名一致。
     */
    @FunctionalInterface
    public interface Parse {
        /**
         * 执行解析
         * @param bytes
         * @return
         * @throws InvalidProtocolBufferException
         */
        Message process(byte[] bytes) throws InvalidProtocolBufferException;
    }

}
