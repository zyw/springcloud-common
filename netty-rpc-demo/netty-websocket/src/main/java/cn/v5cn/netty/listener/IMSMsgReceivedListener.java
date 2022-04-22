package cn.v5cn.netty.listener;

import cn.v5cn.netty.bean.IMSMsg;

public interface IMSMsgReceivedListener {
    void onMsgReceived(IMSMsg msg);
}
