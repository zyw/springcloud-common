package cn.v5cn.cim.common.protocol;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author crossoverJie
 */
public class ProtocolUtil {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        CIMRequestProto.CIMReqProtocol protocol = CIMRequestProto.CIMReqProtocol.newBuilder()
                .setRequestId(123L)
                .setReqMsg("你好啊")
                .build();

        byte[] encode = encode(protocol);

        CIMRequestProto.CIMReqProtocol parseFrom = decode(encode);

        System.out.println(protocol.toString());
        System.out.println(protocol.toString().equals(parseFrom.toString()));
    }

    public static byte[] encode(CIMRequestProto.CIMReqProtocol protocol) {
        return protocol.toByteArray();
    }

    public static CIMRequestProto.CIMReqProtocol decode(byte[] bytes) throws InvalidProtocolBufferException {
        return CIMRequestProto.CIMReqProtocol.parseFrom(bytes);
    }
}
