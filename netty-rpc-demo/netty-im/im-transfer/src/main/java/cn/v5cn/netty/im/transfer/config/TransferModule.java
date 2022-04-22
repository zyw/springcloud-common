package cn.v5cn.netty.im.transfer.config;

import cn.v5cn.netty.im.user_status.factory.UserStatusServiceFactory;
import cn.v5cn.netty.im.user_status.service.UserStatusService;
import cn.v5cn.netty.im.user_status.service.impl.RedisUserStatusServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Guice Module模块初始化
 * @author yrw
 */
public class TransferModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
                .implement(UserStatusService.class, RedisUserStatusServiceImpl.class)
                .build(UserStatusServiceFactory.class));
    }
}
