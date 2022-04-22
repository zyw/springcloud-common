package cn.v5cn.guice.demo.test1;

import javax.inject.Singleton;
import javax.inject.Inject;

@Singleton
public class MyApp implements Application {

    private UserService userService;
    private LogService logService;

    @Inject
    public MyApp(UserService userService,LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    public void work() {
        this.userService.process();
        this.logService.log("程序正常运行");
    }
}
