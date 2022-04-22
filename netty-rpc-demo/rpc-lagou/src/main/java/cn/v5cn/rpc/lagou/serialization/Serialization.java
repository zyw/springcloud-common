package cn.v5cn.rpc.lagou.serialization;

import java.io.IOException;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-09-09 22:12
 */
public interface Serialization {
    /**
     * 序列化接口
     * @param obj 待序列化对象
     * @param <T> 序列化类型
     * @return 返回序列化后的字节数组
     * @throws IOException
     */
    <T> byte[] serialize(T obj)throws IOException;

    /**
     * 反序列化
     * @param data 待反序列化字节数组
     * @param clz 反序列化类型
     * @param <T> 反序列化类型
     * @return 返回序列后的对象
     * @throws IOException
     */
    <T> T deSerialize(byte[] data, Class<T> clz)throws IOException;
}
