package cn.v5cn.netty.im.rest.spi;

import cn.v5cn.netty.im.rest.spi.domain.UserBase;

public interface UserSpi<T extends UserBase> {

    /**
     * get user by username and password, return user(id can not be null) if username and password are right, else return null.
     * be sure that your password has been properly encrypted
     *
     * 通过用户名和密码获取用户，如果用户名和密码正确，则返回用户（id不能为null），否则返回null。
     * 确保您的密码已正确加密
     * @param username
     * @param pwd
     * @return
     */
    T getUser(String username, String pwd);

    /**
     * get user by id, if id not exist then return null.
     * 按id获取用户，如果id不存在，则返回null。
     * @param id
     * @return
     */
    T getById(String id);
}
