package cn.v5cn.liyue.rpc.netty.serialize.impl;

import cn.v5cn.liyue.rpc.netty.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;

public class ObjectSerializer implements Serializer<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectSerializer.class);

    @Override
    public int size(Object entry) {
        return Integer.BYTES + toBytes(entry).length;
    }

    @Override
    public void serialize(Object entry, byte[] bytes, int offset, int length) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        final byte[] tmpBytes = toBytes(entry);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);
    }

    @Override
    public Object parse(byte[] bytes, int offset, int length) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            final ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
            final int len = buffer.getInt();
            byte[] tmpBytes = new byte[len];
            buffer.get(tmpBytes);
            bis = new ByteArrayInputStream(tmpBytes);
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

    @Override
    public byte type() {
        return Types.TYPE_OBJECT;
    }

    @Override
    public Class<Object> getSerializeClass() {
        return Object.class;
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
}
