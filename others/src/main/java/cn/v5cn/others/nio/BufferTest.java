package cn.v5cn.others.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-16 16:28
 */
public class BufferTest {
    public static void main(String[] args) {
        //创建Buffer
        IntBuffer buffer = IntBuffer.allocate(10);

        for(int i = 0; i < buffer.capacity(); i++) {
            int rodm = new SecureRandom().nextInt(20);
            //写入Buffer
            buffer.put(rodm);
        }
        //Buffer读写反转
        buffer.flip();

        while (buffer.hasRemaining()) {
            //读取Buffer
            System.out.println(buffer.get());
        }
    }
}
