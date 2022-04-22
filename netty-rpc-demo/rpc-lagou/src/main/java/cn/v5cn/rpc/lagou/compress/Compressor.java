package cn.v5cn.rpc.lagou.compress;

import java.io.IOException;

/**
 * 压缩与解压抽象接口
 * @author ZYW
 * @version 1.0
 * @date 2020-09-09 22:26
 */
public interface Compressor {
    /**
     * 压缩
     * @param array
     * @return
     * @throws IOException
     */
    byte[] compress(byte[] array) throws IOException;

    /**
     * 解压缩
     * @param array
     * @return
     * @throws IOException
     */
    byte[] unCompress(byte[] array) throws IOException;
}
