package cn.v5cn.liyue.rpc.netty.serialize.json;

import cn.v5cn.liyue.rpc.netty.nameservice.Metadata;
import cn.v5cn.liyue.rpc.netty.serialize.Serializer;
import cn.v5cn.liyue.rpc.netty.serialize.impl.Types;
import cn.v5cn.liyue.rpc.netty.server.RpcRequestHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetadataJSONSerializer implements Serializer<Metadata> {

    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    @Override
    public int size(Metadata entry) {
        return Integer.BYTES + JSON.toJSONBytes(entry).length;
    }

    @Override
    public void serialize(Metadata entry, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        byte[] jsonBytes = JSON.toJSONBytes(entry);
        buffer.putInt(jsonBytes.length);
        buffer.put(jsonBytes);
    }

    @Override
    public Metadata parse(byte[] bytes, int offset, int length) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
            final int len = buffer.getInt();
            byte[] tmpBytes = new byte[len];
            buffer.get(tmpBytes);
            Map<String,Object> map = JSONObject.parseObject(tmpBytes, Map.class);
            Metadata metadata = new Metadata();
            List<URI> mdUri;
            for(String uriStr : map.keySet()) {
                List<String> values = (List<String>) map.get(uriStr);
                mdUri = new ArrayList<>();
                for (String value : values) {
                    mdUri.add(new URI(value));
                }
                metadata.put(uriStr,mdUri);
            }
            return metadata;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.warn(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public byte type() {
        return Types.TYPE_METADATA;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return Metadata.class;
    }
}
