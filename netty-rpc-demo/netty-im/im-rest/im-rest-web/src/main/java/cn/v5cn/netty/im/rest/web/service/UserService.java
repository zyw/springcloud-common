package cn.v5cn.netty.im.rest.web.service;

import cn.v5cn.netty.im.common.domain.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    /**
     * 验证用户密码，成功则返回用户，失败返回null
     *
     * @param username 用户名
     * @param pwd      密码
     * @return
     */
    User verifyAndGet(String username, String pwd);
}
