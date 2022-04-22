package cn.v5cn.cim.route.api.vo.req;

import cn.v5cn.cim.common.req.BaseRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author crossoverJie
 */
public class RegisterInfoReqVO extends BaseRequest {

    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(required = true, value = "userName", example = "zhangsan")
    private String userName ;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "RegisterInfoReqVO{" +
                "userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
