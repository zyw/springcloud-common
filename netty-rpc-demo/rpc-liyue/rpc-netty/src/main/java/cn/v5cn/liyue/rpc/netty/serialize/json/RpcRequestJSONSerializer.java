package cn.v5cn.liyue.rpc.netty.serialize.json;

import cn.v5cn.liyue.rpc.netty.client.stubs.RpcRequest;
import cn.v5cn.liyue.rpc.netty.serialize.Serializer;
import cn.v5cn.liyue.rpc.netty.serialize.impl.Types;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.ByteBuffer;

/**
 *
 */
public class RpcRequestJSONSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest entry) {
        return Integer.BYTES + JSON.toJSONBytes(entry).length;
    }

    @Override
    public void serialize(RpcRequest entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] jsonBytes = JSON.toJSONBytes(entry);
        buffer.putInt(jsonBytes.length);
        buffer.put(jsonBytes);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int len = buffer.getInt();
        byte[] tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        return JSONObject.parseObject(tmpBytes,RpcRequest.class);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }
}
