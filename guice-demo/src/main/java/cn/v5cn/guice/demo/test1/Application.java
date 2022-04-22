package cn.v5cn.guice.demo.test1;

import com.google.inject.ImplementedBy;

@ImplementedBy(MyApp.class)
public interface Application {
    void work();
}
