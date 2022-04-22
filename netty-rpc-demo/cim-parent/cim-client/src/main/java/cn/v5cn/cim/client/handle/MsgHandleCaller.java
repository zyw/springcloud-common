package cn.v5cn.cim.client.handle;

import cn.v5cn.cim.client.service.CustomMsgHandleListener;

/**
 * 消息回调 bean
 * @author crossoverJie
 */
public class MsgHandleCaller {

    /**
     * 回调接口
     */
    private CustomMsgHandleListener msgHandleListener;

    public MsgHandleCaller(CustomMsgHandleListener msgHandleListener) {
        this.msgHandleListener = msgHandleListener;
    }

    public CustomMsgHandleListener getMsgHandleListener() {
        return this.msgHandleListener;
    }

    public void setMsgHandleListener(CustomMsgHandleListener msgHandleListener) {
        this.msgHandleListener = msgHandleListener;
    }
}
