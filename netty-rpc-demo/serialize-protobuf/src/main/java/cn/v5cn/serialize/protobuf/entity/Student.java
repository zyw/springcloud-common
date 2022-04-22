package cn.v5cn.serialize.protobuf.entity;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 14:25
 */
public class Student {
    private  String name;
    private  int    age;
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
