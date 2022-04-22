package cn.v5cn.security2.security.vo;

import java.io.Serializable;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 21:31
 */
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }
    public String getToken() {
        return this.jwttoken;
    }
}
