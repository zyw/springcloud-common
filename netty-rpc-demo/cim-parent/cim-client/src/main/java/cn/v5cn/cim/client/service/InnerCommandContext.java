package cn.v5cn.cim.client.service;

import cn.v5cn.cim.client.service.impl.command.PrintAllCommand;
import cn.v5cn.cim.client.util.SpringBeanFactory;
import cn.v5cn.cim.common.enums.SystemCommandEnum;
import cn.v5cn.cim.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author crossoverJie
 */
@Component
public class InnerCommandContext {
    private final static Logger LOGGER = LoggerFactory.getLogger(InnerCommandContext.class);

    /**
     * 获取执行器实行
     * @param command 执行器实例
     * @return
     */
    public InnerCommand getInstance(String command) {
        Map<String, String> allClazz = SystemCommandEnum.getAllClazz();

        String[] trim = command.trim().split(" ");
        String clazz = allClazz.get(trim[0]);
        InnerCommand innerCommand = null;
        try {
            if (StringUtil.isEmpty(clazz)) {
                clazz = PrintAllCommand.class.getName();
            }
            innerCommand = (InnerCommand) SpringBeanFactory.getBean(Class.forName(clazz));
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }

        return innerCommand;
    }
}
