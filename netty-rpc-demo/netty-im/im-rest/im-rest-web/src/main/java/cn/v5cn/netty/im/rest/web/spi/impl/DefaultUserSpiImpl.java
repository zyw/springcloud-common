package cn.v5cn.netty.im.rest.web.spi.impl;

import cn.v5cn.netty.im.common.domain.po.User;
import cn.v5cn.netty.im.rest.spi.UserSpi;
import cn.v5cn.netty.im.rest.spi.domain.UserBase;
import cn.v5cn.netty.im.rest.web.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserSpiImpl implements UserSpi<UserBase> {

    private UserService userService;

    public DefaultUserSpiImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserBase getUser(String username, String pwd) {
        final User user = userService.verifyAndGet(username, pwd);
        if(user == null) {
            return null;
        }

        UserBase userBase = new UserBase();
        userBase.setId(user.getId() + "");
        userBase.setUsername(user.getUsername());
        return  userBase;
    }

    @Override
    public UserBase getById(String id) {
        final User user = userService.getById(Long.parseLong(id));
        if(user == null) {
            return null;
        }

        UserBase userBase = new UserBase();
        userBase.setId(userBase.getId());
        userBase.setUsername(userBase.getUsername());
        return userBase;
    }
}
