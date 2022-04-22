package cn.v5cn.cim.client.service;

/**
 * 自定义消息回调
 * @author crossoverJie
 */
public interface CustomMsgHandleListener {

    /**
     * 消息回调
     * @param msg
     */
    void handle(String msg);
}
