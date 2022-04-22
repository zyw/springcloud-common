package cn.v5cn.guice.demo.test1;

import com.google.inject.ImplementedBy;

@ImplementedBy(UserServiceImpl.class)
public interface UserService {
    void process();
}
