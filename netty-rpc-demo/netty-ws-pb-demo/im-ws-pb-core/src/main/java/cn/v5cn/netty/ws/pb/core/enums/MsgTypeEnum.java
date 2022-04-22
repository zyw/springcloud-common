package cn.v5cn.netty.ws.pb.core.enums;

import cn.v5cn.netty.ws.pb.core.entity.AckMsg;
import cn.v5cn.netty.ws.pb.core.entity.ChatMsg;
import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;

import java.util.stream.Stream;

/**
 * @author powertime
 */

public enum MsgTypeEnum {
    /**
     * 聊天消息
     */
    CHAT(0, ChatMsg.class),
    /**
     * 内部消息
     */
    INTERNAL(1, InternalMsg.class),
    /**
     * ack消息
     */
    ACK(2, AckMsg.class);


    int code;
    Class<?> clazz;
    MsgTypeEnum(int code, Class<?> clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public static MsgTypeEnum getByCode(int code) {
        return Stream.of(values()).filter(t -> t.code == code)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static MsgTypeEnum getByClass(Class<?> clazz) {
        return Stream.of(values()).filter(t -> t.clazz == clazz)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public int getCode() {
        return code;
    }
}
