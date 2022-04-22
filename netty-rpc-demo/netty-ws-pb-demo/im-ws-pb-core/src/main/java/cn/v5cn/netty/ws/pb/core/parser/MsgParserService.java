package cn.v5cn.netty.ws.pb.core.parser;

import cn.v5cn.netty.ws.pb.core.entity.AckMsg;
import cn.v5cn.netty.ws.pb.core.entity.ChatMsg;
import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;
import cn.v5cn.netty.ws.pb.core.enums.MsgTypeEnum;
import cn.v5cn.netty.ws.pb.core.exception.ImLogicException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * protobuf解析消息，不同消息解析不同的类
 * @author powertime
 */
public class MsgParserService {

    private final Map<MsgTypeEnum, Parse> parseMsgMap;

    public MsgParserService() {
        parseMsgMap = new HashMap<>(MsgTypeEnum.values().length);

        // Message中的parseFrom通过lambda表达式::变成Parse函数式接口对象。
        parseMsgMap.put(MsgTypeEnum.CHAT, ChatMsg::parseFrom);
        parseMsgMap.put(MsgTypeEnum.ACK, AckMsg::parseFrom);
        parseMsgMap.put(MsgTypeEnum.INTERNAL, InternalMsg::parseFrom);
    }

    public Message getMsgByCode(int code, byte[] bytes) throws InvalidProtocolBufferException {
        MsgTypeEnum msgType = MsgTypeEnum.getByCode(code);
        Parse parse = parseMsgMap.get(msgType);
        if(parse == null) {
            throw new ImLogicException("没有获得到消息解析器，解析码：" + code + "，消息类型：" + msgType.name());
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
