package cn.v5cn.phoenix.demo;

import cn.v5cn.phoenix.demo.entity.User;
import cn.v5cn.phoenix.demo.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-06 23:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void saveUserTest() {
        User u = new User();
        u.setId(5);
        u.setName("猪八戒");
        u.setAge(400);

        userMapper.upsert(u);
    }

    @Test
    public void findByIdTest() {
        User user = userMapper.findById(5);
        System.out.println(user);
    }

    @Test
    public void findByNameTest() {
        User user = userMapper.findByName("zhangsan");
        System.out.println(user);
    }

    @Test
    public void findByAgeTest() {
        List<User> users = userMapper.findByAge(20);
        users.forEach(System.out::println);
    }

    @Test
    public void findAllTest() {
        List<User> users = userMapper.findAll();
        users.forEach(System.out::println);
    }

    @Test
    public void deleteTest() {
        System.out.println(userMapper.delete(5));
    }
}
