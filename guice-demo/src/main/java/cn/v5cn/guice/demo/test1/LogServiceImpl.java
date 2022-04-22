package cn.v5cn.guice.demo.test1;


import javax.inject.Singleton;

@Singleton
public class LogServiceImpl implements LogService {
    @Override
    public void log(String msg) {
        System.out.println("------LOG:" + msg);
    }
}
