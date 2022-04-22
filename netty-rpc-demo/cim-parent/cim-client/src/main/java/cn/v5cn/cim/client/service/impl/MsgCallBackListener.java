package cn.v5cn.cim.client.service.impl;

import cn.v5cn.cim.client.service.CustomMsgHandleListener;
import cn.v5cn.cim.client.service.MsgLogger;
import cn.v5cn.cim.client.util.SpringBeanFactory;

/**
 * 自定义收到消息回调
 * @author crossoverJie
 */
public class MsgCallBackListener implements CustomMsgHandleListener {

    private MsgLogger msgLogger;

    public MsgCallBackListener() {
        this.msgLogger = SpringBeanFactory.getBean(MsgLogger.class);
    }

    @Override
    public void handle(String msg) {
        msgLogger.log(msg);
    }
}
