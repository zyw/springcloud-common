package cn.v5cn.rpc.lagou.serialization;

import cn.v5cn.rpc.lagou.Constants;

import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-09-11 22:35
 */
public class SerializationFactory {
    private static List<Serialization> serializationList = new LinkedList<>();

    static {
        serializationList.addAll(
                StreamSupport.stream(ServiceLoader.load(Serialization.class).spliterator(),false)
                        .collect(Collectors.toList()));
    }

    public static Serialization get(byte extraInfo) {
        String bitStr = Constants.byteToBit(extraInfo);
        int kind = Integer.valueOf(bitStr.substring(1, 3),2);
        if(kind > serializationList.size()) {
            throw new RuntimeException("下标越界");
        }
        return serializationList.get(kind);
    }
}
