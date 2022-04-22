package cn.v5cn.guice.demo.test1;

import com.google.inject.ImplementedBy;

@ImplementedBy(LogServiceImpl.class)
public interface LogService {
    void log(String msg);
}
