package cn.v5cn.liyue.rpc.netty.serialize;

import cn.v5cn.liyue.rpc.api.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SerializeSupport {
    private static final Logger logger = LoggerFactory.getLogger(SerializeSupport.class);
    private static Map<Class<?>/*序列化对象类型*/,Serializer<?>/*序列化实现*/> serializerMap = new HashMap<>();
    private static Map<Byte/*序列化实现类型*/,Class<?>/*序列化对象类型*/> typeMap = new HashMap<>();

    static {
        for (Serializer serializer : ServiceSupport.loadAll(Serializer.class)) {
            registerType(serializer.type(),serializer.getSerializeClass(),serializer);
            logger.info("Found serializer, class: {}, type: {}.",
                    serializer.getSerializeClass().getCanonicalName(),
                    serializer.type());
        }
    }

    private static byte parseEntryType(byte[] buffer) {
        return buffer[0];
    }

    private static <E> void registerType(byte type, Class<E> eClass,Serializer<E> serializer) {
        serializerMap.put(eClass,serializer);
        typeMap.put(type,eClass);
    }

    private static <E> E parse(byte[] buffer,int offset,int length, Class<E> eClass) {
        logger.info("serializer: {}",serializerMap.get(eClass));
        Object entry = serializerMap.get(eClass).parse(buffer, offset, length);
        if(eClass.isAssignableFrom(entry.getClass())) {
            return (E)entry;
        } else {
            throw new SerializeException("Type mismatch!");
        }
    }

    public static <E> E parse(byte[] buffer) {
        return parse(buffer,0,buffer.length);
    }

    private static <E> E parse(byte[] buffer,int offset, int length) {
        byte type = parseEntryType(buffer);
        logger.info("----------------------------{}",type);
        Class<E> eClass = (Class<E>)typeMap.get(type);
        if(null == eClass) {
            throw new SerializeException(String.format("Unknown entry type: %d!", type));
        } else {
            return parse(buffer, offset + 1, length - 1,eClass);
        }
    }

    public static <E> byte[] serialize(E entry) {
        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if(serializer == null) {
            throw new SerializeException(String.format("Unknown entry class type: %s", entry.getClass().toString()));
        }
        byte[] bytes = new byte[serializer.size(entry) + 1];
        bytes[0] = serializer.type();
        serializer.serialize(entry,bytes,1,bytes.length - 1);
        return bytes;
    }
}
