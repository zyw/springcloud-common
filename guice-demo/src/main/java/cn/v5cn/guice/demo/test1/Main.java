package cn.v5cn.guice.demo.test1;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    public static void main(String[] args) {
        //第一种非注解方式
        //Injector injector = Guice.createInjector(new MyAppModule());
        // 注解方式 @Singleton 和 @ImplementedBy
        Injector injector = Guice.createInjector();
        Application application = injector.getInstance(Application.class);

        application.work();

    }
}
