package cn.v5cn.cim.route.api;

import cn.v5cn.cim.common.res.BaseResponse;
import cn.v5cn.cim.route.api.vo.req.ChatReqVO;
import cn.v5cn.cim.route.api.vo.req.LoginReqVO;
import cn.v5cn.cim.route.api.vo.req.P2PReqVO;
import cn.v5cn.cim.route.api.vo.req.RegisterInfoReqVO;
import cn.v5cn.cim.route.api.vo.res.RegisterInfoResVO;

/**
 * @author crossoverJie
 */
public interface RouteApi {

    /**
     * group chat
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object groupRoute(ChatReqVO groupReqVO) throws Exception;

    /**
     * Point to point chat
     * @param p2PReqVO
     * @return
     * @throws Exception
     */
    Object p2pRoute(P2PReqVO p2PReqVO) throws Exception;

    /**
     * Offline account
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object offLine(ChatReqVO groupReqVO) throws Exception;

    /**
     * login account
     * @param loginReqVO
     * @return
     * @throws Exception
     */
    Object login(LoginReqVO loginReqVO) throws Exception;

    /**
     * Register account
     * @param registerInfoReqVO
     * @return
     * @throws Exception
     */
    BaseResponse<RegisterInfoResVO> registerAccount(RegisterInfoReqVO registerInfoReqVO) throws Exception;

    /**
     * Get all online users
     * @return
     * @throws Exception
     */
    Object onlineUser() throws Exception;
}
