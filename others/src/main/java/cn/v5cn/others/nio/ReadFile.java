package cn.v5cn.others.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-16 21:07
 */
public class ReadFile {
    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("./others/ReadFile.txt");
        FileChannel channel = inputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        int read = channel.read(buffer);

        buffer.flip();

        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.println((char) b);
        }
    }
}
