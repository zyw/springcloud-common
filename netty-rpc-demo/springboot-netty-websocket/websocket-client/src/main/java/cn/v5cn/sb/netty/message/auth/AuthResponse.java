package cn.v5cn.sb.netty.message.auth;

import cn.v5cn.sb.netty.common.dispacher.Message;

/**
 * 用户认证响应
 * @author ZYW
 * @version 1.0
 * @date 2021/9/4 9:24 下午
 */
public class AuthResponse implements Message {

    public static final String TYPE = "AUTH_RESPONSE";

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 响应提示
     */
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
