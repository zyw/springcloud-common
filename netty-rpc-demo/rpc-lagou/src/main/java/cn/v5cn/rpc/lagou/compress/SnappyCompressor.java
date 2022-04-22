package cn.v5cn.rpc.lagou.compress;

import org.xerial.snappy.Snappy;

import java.io.IOException;

/**
 * Snappy实现的压缩算法
 * 一个基于 Snappy 压缩算法的实现
 * @author ZYW
 * @version 1.0
 * @date 2020-09-09 22:28
 */
public class SnappyCompressor implements Compressor {
    @Override
    public byte[] compress(byte[] array) throws IOException {
        if(array == null) {
            return null;
        }
        return Snappy.compress(array);
    }

    @Override
    public byte[] unCompress(byte[] array) throws IOException {
        if(array == null) {
            return null;
        }
        return Snappy.uncompress(array);
    }
}
