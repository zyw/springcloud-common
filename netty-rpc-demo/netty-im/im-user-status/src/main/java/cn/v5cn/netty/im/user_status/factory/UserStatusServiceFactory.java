package cn.v5cn.netty.im.user_status.factory;

import cn.v5cn.netty.im.user_status.service.UserStatusService;

import java.util.Properties;

/**
 * @author yrw
 */
@FunctionalInterface
public interface UserStatusServiceFactory {
    /**
     * create a userStatusService
     *
     * @param properties
     * @return
     */
    UserStatusService createService(Properties properties);
}
