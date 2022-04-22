package cn.v5cn.springboot.cache.j2cache.controller;

import cn.v5cn.springboot.cache.j2cache.entity.User;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZYW
 */
@RestController
public class IndexController {

    @Autowired
    private CacheChannel cacheChannel;

    @GetMapping("/index")
    public Object index(){
        CacheObject cacheObject = cacheChannel.get("default", "user");
        User user = null;
        if(cacheObject.getValue() == null) {
            user = new User();
            user.setId(1234L);
            user.setName("张三");
            user.setAge(19);
            cacheChannel.set("default","user",user);
            System.out.println("设置了！");
        } else {
            byte level = cacheObject.getLevel();
            System.out.println(Integer.valueOf(level));
            user = (User)cacheObject.getValue();
        }

        return user;
    }

}
