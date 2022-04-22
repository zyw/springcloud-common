package cn.v5cn.cim.client.vo.req;

import cn.v5cn.cim.common.req.BaseRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author crossoverJie
 */
public class SendMsgReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    @ApiModelProperty(required = true, value = "msg", example = "hello")
    private String msg ;

    @NotNull(message = "userId 不能为空")
    @ApiModelProperty(required = true, value = "userId", example = "11")
    private Long userId ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
