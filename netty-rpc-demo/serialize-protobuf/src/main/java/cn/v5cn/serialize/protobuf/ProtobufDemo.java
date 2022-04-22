package cn.v5cn.serialize.protobuf;

/**
 * Protobuf序列化，PersonProtobuf是person.proto生成的文件
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 14:17
 */
public class ProtobufDemo {
    public static void main(String[] args) {
        PersonProtobuf.Person johnDoe = PersonProtobuf.Person.newBuilder()
                .setId(1234)
                .setName("John Doe")
                .setEmail("johndoe@gmail.com")
                .build();
        byte[] bytes = johnDoe.toByteArray();
        System.out.println(bytes.length);
        //johnDoe.writeTo(); 写入文件
    }
}
