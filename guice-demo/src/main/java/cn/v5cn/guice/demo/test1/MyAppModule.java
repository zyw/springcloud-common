package cn.v5cn.guice.demo.test1;

import com.google.inject.AbstractModule;

public class MyAppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(LogService.class).to(LogServiceImpl.class);
        bind(Application.class).to(MyApp.class);
    }
}
