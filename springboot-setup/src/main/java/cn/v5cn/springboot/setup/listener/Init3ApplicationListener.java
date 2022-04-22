package cn.v5cn.springboot.setup.listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * ApplicationListener监听不同的事件，在不同的事件触发时调用下面的方法，获取不同的事件内容
 */
public class Init3ApplicationListener implements ApplicationListener<ApplicationStartedEvent>, Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("Init3ApplicationListener-------------------------------------");
    }
}
