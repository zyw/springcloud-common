package cn.v5cn.netty.im.connector.config;

import cn.v5cn.netty.im.user_status.factory.UserStatusServiceFactory;
import cn.v5cn.netty.im.user_status.service.UserStatusService;
import cn.v5cn.netty.im.user_status.service.impl.RedisUserStatusServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author yrw
 */
public class ConnectorModule extends AbstractModule {

    @Override
    protected void configure() {
        install(
                new FactoryModuleBuilder()
                .implement(UserStatusService.class, RedisUserStatusServiceImpl.class)
                .build(UserStatusServiceFactory.class)
        );

        install(
                new FactoryModuleBuilder()
                .build(ConnectorRestServiceFactory.class)
        );
    }
}
