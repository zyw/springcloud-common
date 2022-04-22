package cn.v5cn.serialize.protobuf;

import cn.v5cn.serialize.protobuf.entity.School;
import cn.v5cn.serialize.protobuf.entity.Student;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用Protostuff来完成protobuf，这样不需要编写proto文件。
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 14:17
 */
public class ProtostuffDemo {

    // 申请buffer空间，避免每次序列化都重新申请Buffer空间
    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    // 缓存Schema
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    // 序列化方法，把指定对象序列化成字节数组
    public static <T> byte[] serialize(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        try {
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    // 反序列化方法，将字节数组反序列化成指定Class类型
    public static <T> T deserialize(byte[] data,Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data,obj,schema);
        return obj;
    }

    //获取schema方法
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            if(schema != null) {
                schemaCache.put(clazz,schema);
            }
        }
        return schema;
    }

    public static void main(String[] args) {
        Student stu1 = new Student("张三",20);
        Student stu2 = new Student("李四",21);
        List<Student> students = new ArrayList<Student>();
        students.add(stu1);
        students.add(stu2);
        School school = new School("西工大",students);
        //首先是序列化
        byte[] bytes = ProtostuffDemo.serialize(school);
        System.out.println("序列化后: " + bytes.length);
        //然后是反序列化
        School group1 = ProtostuffDemo.deserialize(bytes,School.class);
        System.out.println("反序列化后: " + school.toString());
    }
}
