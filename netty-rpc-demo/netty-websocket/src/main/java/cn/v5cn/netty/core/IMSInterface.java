package cn.v5cn.netty.core;

import cn.v5cn.netty.bean.IMSMsg;
import cn.v5cn.netty.config.IMSOptions;
import cn.v5cn.netty.listener.IMSMsgReceivedListener;

public interface IMSInterface {
    /**
     * 初始化
     *
     * @param options
     * @return
     */
    boolean init(IMSOptions options, IMSMsgReceivedListener msgReceivedListener);

    /**
     * 启动IMS
     */
    void start();

    /**
     * 发送消息
     *
     * @param msg
     */
    void sendMsg(IMSMsg msg);

    /**
     * 发送消息
     * 重载
     *
     * @param msg
     * @param isJoinResendManager 是否加入消息重发管理器
     */
    void sendMsg(IMSMsg msg, boolean isJoinResendManager);

    /**
     * 释放资源
     */
    void release();
}
