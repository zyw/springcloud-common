package cn.v5cn.others.nio;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class FileChannelTest2 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("./others/fromFile.txt","rw");
        final FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("./others/toFile.txt","rw");
        final FileChannel toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();
        // 来自一个channel的数据
//        toChannel.transferFrom(fromChannel,position,count);
        // 把channel的数据写入另一个channel
        fromChannel.transferTo(position,count,toChannel);


        /**
         * ===============================================================
         *                         Selector练习
         * ===============================================================
         */
        // 创建channel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8888));
        // 创建Selector
        final Selector selector = Selector.open();
        ssc.configureBlocking(false);
        SelectionKey key = ssc.register(selector, SelectionKey.OP_READ);
        while (true) {
            final int readyChannels = selector.select();

            if(readyChannels == 0) continue;

            final Set<SelectionKey> selectionKeys = selector.selectedKeys();
            final Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                    System.out.println("channel可以接受连接");
                } else if(selectionKey.isConnectable()) {
                    // a connection was established with a remote server.
                    System.out.println("channel已经连接上");
                } else if(selectionKey.isReadable()) {
                    // a channel is ready for reading
                    System.out.println("channel可以读取数据");
                } else if(selectionKey.isWritable()) {
                    // a channel is ready for writing
                    System.out.println("channel可以写数据");
                }
                iterator.remove();
            }
        }
    }
}
