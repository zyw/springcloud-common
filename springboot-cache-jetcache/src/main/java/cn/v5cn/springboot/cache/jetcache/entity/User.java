package cn.v5cn.springboot.cache.jetcache.entity;

import java.io.Serializable;

/**
 * @author ZYW
 */
public class User implements Serializable {

    public User() {
        System.out.println("User对象初始化了！！！！！");
    }

    private Long id;
    private String name;
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
