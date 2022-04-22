package cn.v5cn.springboot.setup.listener;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

public class InitApplicationListener implements ApplicationListener<ApplicationContextInitializedEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        System.out.println("initApplicationListener------------------------------------------------------------");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
