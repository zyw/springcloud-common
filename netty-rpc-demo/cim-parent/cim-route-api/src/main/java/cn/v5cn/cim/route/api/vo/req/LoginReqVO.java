package cn.v5cn.cim.route.api.vo.req;

import cn.v5cn.cim.common.req.BaseRequest;

/**
 * @author crossoverJie
 */
public class LoginReqVO extends BaseRequest {

    private Long userId ;
    private String userName ;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LoginReqVO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                "} " + super.toString();
    }
}
