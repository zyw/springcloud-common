package cn.v5cn.rpc.lagou.serialization;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 使用Hessian实现序列化与反序列化
 * @author ZYW
 * @version 1.0
 * @date 2020-09-09 22:18
 */
public class HessianSerialization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(os);
        hessian2Output.writeObject(obj);
        return os.toByteArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deSerialize(byte[] data, Class<T> clz) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(is);

        return (T)hessian2Input.readObject(clz);
    }
}
