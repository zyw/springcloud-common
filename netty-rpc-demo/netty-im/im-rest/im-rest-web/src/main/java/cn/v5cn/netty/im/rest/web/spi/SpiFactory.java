package cn.v5cn.netty.im.rest.web.spi;

import cn.v5cn.netty.im.common.exception.ImException;
import cn.v5cn.netty.im.rest.spi.UserSpi;
import cn.v5cn.netty.im.rest.spi.domain.UserBase;
import cn.v5cn.netty.im.rest.web.spi.impl.DefaultUserSpiImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SpiFactory implements ApplicationContextAware {

    private UserSpi<? extends UserBase> userSpi;
    private ApplicationContext applicationContext;

    @Value("${spi.user.impl.class}")
    private String userSpiImplClassName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public UserSpi<? extends UserBase> getUserSpi() {
        if(StringUtils.isEmpty(userSpiImplClassName)) {
            return applicationContext.getBean(DefaultUserSpiImpl.class);
        }
        try {
            if(userSpi == null) {
                final Class<?> userSpiImplClass = Class.forName(userSpiImplClassName);
                userSpi = (UserSpi<? extends UserBase>)applicationContext.getBean(userSpiImplClass);
            }
            return userSpi;
        } catch (ClassNotFoundException e) {
            throw new ImException("can not find class: " + userSpiImplClassName);
        }
    }
}
