package cn.v5cn.serialize.hessian;

import com.caucho.hessian.io.Hessian2Output;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-24 21:36
 */
public class SerializeTest {

    public static void serializeHessian2() {
        Student student = new Student(1, "hessian", "boy");
        byte[] bytes = Serializes.serializeHessian2(student);
        System.out.println("Hessian2序列化结果长度 = " + bytes.length);

        Student s1 = Serializes.deserialize2(bytes);
        System.out.println("反序列化2：" + s1);
    }

    public static void serializeHessian() {
        Student student = new Student(1, "hessian", "boy");
        byte[] bytes = Serializes.serializeHessian(student);
        System.out.println("Hessian序列化结果长度 = " + bytes.length);


        Student s1 = Serializes.deserialize(bytes);
        System.out.println("反序列化：" + s1);
    }

    @Test
    public void serialize() {
        SerializeTest.serializeHessian2();
        SerializeTest.serializeHessian();

//        Hessian2序列化结果长度 = 59
//        反序列化2：User(id=1,name=hessian,gender=null)

//        Hessian序列化结果长度 = 65
//        反序列化：User(id=1,name=hessian,gender=null)
    }
}
