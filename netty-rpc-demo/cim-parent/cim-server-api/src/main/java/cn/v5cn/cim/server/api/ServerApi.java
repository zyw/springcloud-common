package cn.v5cn.cim.server.api;

import cn.v5cn.cim.server.api.vo.req.SendMsgReqVO;

/**
 * @author crossoverJie
 */
public interface ServerApi {

    /**
     * Push msg to client
     * @param sendMsgReqVO
     * @return
     * @throws Exception
     */
    Object sendMsg(SendMsgReqVO sendMsgReqVO) throws Exception;
}
