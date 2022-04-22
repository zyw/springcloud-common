package cn.v5cn.rpc.lagou;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-09-09 21:16
 */
public class Constants {
    public static final int HEADER_SIZE = 16;
    public static final short MAGIC = 1020;
    public static final int HEARTBEAT_CODE = 2;
    public static final int DEFAULT_IO_THREADS = 2;
    public static final byte VERSION1_0 = 1;
    public static final long DEFAULT_TIMEOUT = 5000;

    public static boolean isHeartBeat(byte extraInfo) {
        String byteStr = byteToBit(extraInfo);
        String substring = byteStr.substring(5, 7);
        return Integer.valueOf(substring,2) == 0;
    }

    /**
     * Integer.valueOf("01100000", 2).byteValue() //把字符串转化成byte
     * 把byte转化为bit字符串
     * @param b byte
     * @return bit字符串
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b) & 0x1);
    }

    public static void main(String[] args) {
        final byte byteValue = Integer.valueOf("01111111", 2).byteValue();
        System.out.println(Integer.valueOf(Constants.byteToBit(byteValue).substring(3, 5),2));
    }
}
