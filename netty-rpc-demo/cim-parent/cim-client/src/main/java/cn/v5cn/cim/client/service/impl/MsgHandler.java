package cn.v5cn.cim.client.service.impl;

import cn.v5cn.cim.client.client.CIMClient;
import cn.v5cn.cim.client.config.AppConfiguration;
import cn.v5cn.cim.client.service.*;
import cn.v5cn.cim.client.vo.req.GroupReqVO;
import cn.v5cn.cim.client.vo.req.P2PReqVO;
import cn.v5cn.cim.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author crossoverJie
 */
@Service
public class MsgHandler implements MsgHandle {

    private final static Logger LOGGER = LoggerFactory.getLogger(MsgHandler.class);

    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private AppConfiguration appConfiguration;

    @Resource(name = "callBackThreadPool")
    private ThreadPoolExecutor executor;

    @Autowired
    private CIMClient cimClient;

    @Autowired
    private MsgLogger msgLogger;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private InnerCommandContext innerCommandContext;

    private boolean aiModel = false;

    @Override
    public void sendMsg(String msg) {
        if (aiModel) {
            aiChat(msg);
        } else {
            normalChat(msg);
        }
    }

    /**
     * 正常聊天
     * @param msg
     */
    private void normalChat(String msg) {
        String[] totalMsg = msg.split(";;");
        if (totalMsg.length > 1) {
            // 私聊
            P2PReqVO p2PReqVO = new P2PReqVO();
            p2PReqVO.setUserId(appConfiguration.getUserId());
            p2PReqVO.setReceiveUserId(Long.parseLong(totalMsg[0]));
            p2PReqVO.setMsg(totalMsg[1]);
            try {
                p2pChat(p2PReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }
        } else {
            // 群聊
            GroupReqVO groupReqVO = new GroupReqVO(appConfiguration.getUserId(), msg);
            try {
                groupChat(groupReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }
        }
    }

    /**
     * AI model
     * @param msg
     */
    private void aiChat(String msg) {
        msg = msg.replace("吗", "");
        msg = msg.replace("嘛", "");
        msg = msg.replace("?", "!");
        msg = msg.replace("？", "!");
        msg = msg.replace("你", "我");
        System.out.println("AI:\033[31;4m" + msg + "\033[0m");
    }

    @Override
    public void groupChat(GroupReqVO groupReqVO) throws Exception {
        routeRequest.sendGroupMsg(groupReqVO);
    }

    @Override
    public void p2pChat(P2PReqVO p2PReqVO) throws Exception {
        routeRequest.sendP2PMsg(p2PReqVO);
    }

    @Override
    public boolean checkMsg(String msg) {
        if (StringUtil.isEmpty(msg)) {
            LOGGER.warn("不能发送空消息！");
            return true;
        }
        return false;
    }

    @Override
    public boolean innerCommand(String msg) {
        if (msg.startsWith(":")) {
            InnerCommand instance = innerCommandContext.getInstance(msg);
            instance.process(msg);

            return true;
        }
        return false;
    }

    @Override
    public void shutdown() {
        LOGGER.info("系统关闭中。。。");
        routeRequest.offLine();
        msgLogger.stop();
        executor.shutdown();
        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("线程池关闭中。。。。");
            }
            cimClient.close();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
        System.exit(0);
    }

    @Override
    public void openAIModel() {
        aiModel = true;
    }

    @Override
    public void closeAIModel() {
        aiModel = false;
    }
}
