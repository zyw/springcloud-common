package cn.v5cn.springboot.cache.jetcache.service.impl;

import cn.v5cn.springboot.cache.jetcache.entity.User;
import cn.v5cn.springboot.cache.jetcache.service.UserService;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    @Cached(name = "UserService.findUser",cacheType = CacheType.BOTH)
    public User findUser() {
        User user = new User();
        user.setId(1234L);
        user.setName("张三");
        user.setAge(19);
        return user;
    }
}
