package cn.v5cn.liyue.rpc.netty.serialize.impl;

import cn.v5cn.liyue.rpc.netty.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ObjectArraySerializer implements Serializer<Object[]> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectArraySerializer.class);

    @Override
    public int size(Object[] entry) {
        return Integer.BYTES + Arrays.stream(entry).mapToInt(e -> Integer.BYTES + this.toBytes(e).length).sum();
    }

    @Override
    public void serialize(Object[] entry, byte[] bytes, int offset, int length) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        buffer.putInt(entry.length);
        byte[] tmpBytes;
        for (Object o : entry) {
            tmpBytes = this.toBytes(o);
            buffer.putInt(tmpBytes.length);
            buffer.put(tmpBytes);
        }
    }

    @Override
    public Object[] parse(byte[] bytes, int offset, int length) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        final int objLen = buffer.getInt();
        Object[] result = new Object[objLen];
        byte[] tmpBytes;
        for (int i = 0; i < objLen; i++) {
            final int len = buffer.getInt();
            tmpBytes = new byte[len];
            buffer.get(tmpBytes);
            final Object o = toObject(tmpBytes);
            result[i] = o;
        }
        return result;
    }

    @Override
    public byte type() {
        return Types.TYPE_OBJECT_ARRAY;
    }

    @Override
    public Class<Object[]> getSerializeClass() {
        return Object[].class;
    }

    private byte[] toBytes(Object entry) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(entry);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(null != oos) {
                    oos.close();
                }
                if(null != bos) {
                    bos.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
                throw new RuntimeException(e);
            }
        }
    }

    private Object toObject(byte[] bytes) {
        logger.info("======================{}",bytes);
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(null != ois) {
                    ois.close();
                }
                if(null != bis) {
                    bis.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
                throw new RuntimeException(e);
            }
        }
    }
}
