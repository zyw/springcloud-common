package cn.v5cn.guice.demo.test1;

import javax.inject.Singleton;

@Singleton
public class UserServiceImpl implements UserService {
    @Override
    public void process() {
        System.out.println("我需要做一些业务逻辑");
    }
}
