package cn.v5cn.cim.server.controller;

import cn.v5cn.cim.common.constant.Constants;
import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.res.BaseResponse;
import cn.v5cn.cim.route.api.vo.res.SendMsgResVO;
import cn.v5cn.cim.server.api.ServerApi;
import cn.v5cn.cim.server.api.vo.req.SendMsgReqVO;
import cn.v5cn.cim.server.server.CIMServer;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author crossoverJie
 */
@Controller
@RequestMapping("/")
public class IndexController implements ServerApi {

    @Autowired
    private CIMServer cimServer;

    /**
     * 统计 service
     */
    @Autowired
    private CounterService counterService;


    @Override
    @ResponseBody
    @ApiOperation("Push msg to client")
    @RequestMapping(value = "sendMsg", method = RequestMethod.POST)
    public BaseResponse<SendMsgResVO> sendMsg(@RequestBody SendMsgReqVO sendMsgReqVO) throws Exception {
        BaseResponse<SendMsgResVO> res = new BaseResponse<>();
        cimServer.sendMsg(sendMsgReqVO) ;

        counterService.increment(Constants.COUNTER_SERVER_PUSH_COUNT);

        SendMsgResVO sendMsgResVO = new SendMsgResVO() ;
        sendMsgResVO.setMsg("OK") ;
        res.setCode(StatusEnum.SUCCESS.getCode()) ;
        res.setMessage(StatusEnum.SUCCESS.getMessage()) ;
        res.setDataBody(sendMsgResVO) ;

        return res ;
    }
}
