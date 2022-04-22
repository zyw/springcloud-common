package cn.v5cn.phoenix.demo.mapper;

import cn.v5cn.phoenix.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-06 22:57
 */
@Mapper
public interface UserMapper {

    int upsert(@Param("user") User user);

    User findById(@Param("id") int id);

    User findByName(@Param("name") String name);

    List<User> findByAge(@Param("age") int age);

    List<User> findAll();

    int delete(@Param("id") int id);
}
