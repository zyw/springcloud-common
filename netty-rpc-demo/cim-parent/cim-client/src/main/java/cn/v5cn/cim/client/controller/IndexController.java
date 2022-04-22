package cn.v5cn.cim.client.controller;

import cn.v5cn.cim.client.client.CIMClient;
import cn.v5cn.cim.client.service.RouteRequest;
import cn.v5cn.cim.client.vo.req.GoogleProtocolVO;
import cn.v5cn.cim.client.vo.req.GroupReqVO;
import cn.v5cn.cim.client.vo.req.SendMsgReqVO;
import cn.v5cn.cim.client.vo.req.StringReqVO;
import cn.v5cn.cim.client.vo.res.SendMsgResVO;
import cn.v5cn.cim.common.constant.Constants;
import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.res.BaseResponse;
import cn.v5cn.cim.common.res.NULLBody;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private CounterService counterService;

    @Autowired
    private CIMClient heartbeatClient;

    @Autowired
    private RouteRequest routeRequest;


    /**
     * 向服务端发消息 字符串
     * @param stringReqVO
     * @return
     */
    @ApiOperation("客户端发送消息，字符串")
    @RequestMapping(value = "sendStringMsg", method = RequestMethod.POST)
    public BaseResponse<NULLBody> sendStringMsg(StringReqVO stringReqVO) {
        BaseResponse<NULLBody> res = new BaseResponse<>();

        for (int i = 0; i < 100; i++) {
            heartbeatClient.sendStringMsg(stringReqVO.getMsg());
        }

        counterService.increment(Constants.COUNTER_CLIENT_PUSH_COUNT);

        SendMsgResVO sendMsgResVO = new SendMsgResVO();
        sendMsgResVO.setMsg("OK");
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 向服务端发消息 Google ProtoBuf
     * @param googleProtocolVO
     * @return
     */
    @ApiOperation("向服务端发送消息 Google ProtoBuf")
    @RequestMapping(value = "sendProtoBufMsg", method = RequestMethod.POST)
    public BaseResponse<NULLBody> sendProtoBufMsg(GoogleProtocolVO googleProtocolVO) {
        BaseResponse<NULLBody> res = new BaseResponse<>();

        for (int i = 0; i < 100; i++) {
            heartbeatClient.sendGoogleProtocolMsg(googleProtocolVO);
        }

        // 利用 actuator 来自增
        counterService.increment(Constants.COUNTER_CLIENT_PUSH_COUNT);

        SendMsgResVO vo = new SendMsgResVO();
        vo.setMsg("OK");
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 群发消息
     * @param sendMsgReqVO
     * @return
     * @throws Exception
     */
    @ApiOperation("群发消息")
    @RequestMapping(value = "sendGroupMsg", method = RequestMethod.POST)
    public BaseResponse sendGroupMsg(SendMsgReqVO sendMsgReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse<>();

        GroupReqVO groupReqVO = new GroupReqVO(sendMsgReqVO.getUserId(), sendMsgReqVO.getMsg());
        routeRequest.sendGroupMsg(groupReqVO);

        counterService.increment(Constants.COUNTER_CLIENT_PUSH_COUNT);

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.OFF_LINE.getMessage());
        return res;
    }
}
