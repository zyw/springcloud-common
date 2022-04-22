package cn.v5cn.sb.netty.message.auth;

import cn.v5cn.sb.netty.common.dispacher.Message;

/**
 * 用户认证请求
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:24 下午
 */
public class AuthRequest implements Message {

    public static final String TYPE = "AUTH_REQUEST";

    /**
     * 认证 Token
     */
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
