package cn.v5cn.serialize.hessian;

import java.io.Serializable;

/**
 * 无论是JDK自带的序列化还是，Hessian序列化实体类都需要实现序列化接口
 * @author ZYW
 * @version 1.0
 * @date 2020-02-24 21:32
 */
public class Student implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;

    //添加transient关键字，忽略这个字段
    private transient String gender;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public Student() {}

    public Student(int id,String name,String gender){
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User(id="+id+",name="+name+",gender="+gender+")";
    }
}
