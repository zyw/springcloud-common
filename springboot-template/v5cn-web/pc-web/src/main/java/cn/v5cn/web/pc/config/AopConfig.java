package cn.v5cn.web.pc.config;

import cn.v5cn.web.common.log.aop.BusinessLogAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zyw
 */
@Configuration
public class AopConfig {
    /**
     * 日志切面
     *
     * @author xuyuxiang
     * @date 2020/3/20 14:10
     */
    @Bean
    public BusinessLogAop businessLogAop() {
        return new BusinessLogAop();
    }
}
