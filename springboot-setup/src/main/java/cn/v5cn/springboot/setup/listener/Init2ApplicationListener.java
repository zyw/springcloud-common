package cn.v5cn.springboot.setup.listener;

import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * ApplicationListener监听不同的事件，在不同的事件触发时调用下面的方法，获取不同的事件内容
 */
public class Init2ApplicationListener implements ApplicationListener<ApplicationContextInitializedEvent>, Ordered {
    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        System.out.println("Init2ApplicationListener--------------------------------------------");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
