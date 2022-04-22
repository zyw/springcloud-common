package cn.v5cn.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 多种序列化方法
 * @author ZYW
 * @version 1.0
 * @date 2020-02-24 21:41
 */
public class Serializes {

    /**
     * Hessian2是Hessian最新的序列化接口
     * @param t 待序列化的对象
     * @return 返回序号后的二进制数组
     */
    public static <T> byte[] serializeHessian2(T t) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Hessian2Output output = new Hessian2Output(os);

            output.writeObject(t);
            output.getBytesOutputStream().flush();
            output.completeMessage();
            output.close();
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hessian旧API
     * @param t
     * @return
     */
    public static <T> byte[] serializeHessian(T t) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HessianOutput output = new HessianOutput(os);

            output.writeObject(t);
            output.close();
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hessian新API反序列化
     * @param data
     * @return
     */
    public static <T> T deserialize2(byte[] data) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            Hessian2Input input = new Hessian2Input(is);
            return (T)input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hessian旧API反序列化
     * @param data
     * @return
     */
    public static <T> T deserialize(byte[] data) {
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            HessianInput input = new HessianInput(is);
            return (T)input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
