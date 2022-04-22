package cn.v5cn.others.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile accessFile = new RandomAccessFile("./others/FileChannelTest.txt","rw");
        final FileChannel channel = accessFile.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);
        int byteRead = channel.read(buffer);
        while (byteRead != -1) {
            System.out.println("Read: " + byteRead);
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char)buffer.get());
            }
            buffer.clear();
            byteRead = channel.read(buffer);
        }
        accessFile.close();
    }
}
