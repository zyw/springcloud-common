package cn.v5cn.security2.security.vo;

import java.io.Serializable;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-05-26 21:30
 */
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;
    private String username;
    private String password;
    //need default constructor for JSON Parsing
    public JwtRequest()
    {
    }
    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
