package cn.v5cn.others.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-16 21:32
 */
public class WriteFile {
    public static void main(String[] args) throws Exception {
        FileOutputStream outputStream = new FileOutputStream("./others/WriteFile.txt");
        FileChannel channel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);

        byte[] messages = "hello you hello me hello world".getBytes();

        buffer.put(messages);

        buffer.flip();

        channel.write(buffer);

        channel.close();
    }
}
