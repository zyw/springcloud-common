package cn.v5cn.rpc.lagou.compress;

import cn.v5cn.rpc.lagou.Constants;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-09-11 22:40
 */
public class CompressorFactory {

    private static List<Compressor> compressorList = new LinkedList<>();

    static {
        compressorList.addAll(
                StreamSupport.stream(ServiceLoader.load(Compressor.class).spliterator(),false)
                        .collect(Collectors.toList()));
    }

    public static Compressor get(byte extraInfo) {
        String bitStr = Constants.byteToBit(extraInfo);
        int kind = Integer.valueOf(bitStr.substring(3, 5),2);
        if(kind > compressorList.size()) {
            throw new RuntimeException("下标越界");
        }
        return compressorList.get(kind);
    }
}
