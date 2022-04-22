package cn.v5cn.cim.server.api.vo.req;

import cn.v5cn.cim.common.req.BaseRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author crossoverJie
 */
public class SendMsgReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    @ApiModelProperty(required = true, value = "msg", example = "hello")
    private String msg;

    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "userId", example = "11")
    private Long userId;

    public SendMsgReqVO() {
    }

    public SendMsgReqVO(String msg, Long userId) {
        this.msg = msg;
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SendMsgReqVO{" +
                "msg='" + msg + '\'' +
                ", userId=" + userId +
                '}';
    }
}
