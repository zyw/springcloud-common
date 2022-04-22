package cn.v5cn.springcloud.authserver.dto;

import java.io.Serializable;

/**
 * @author ZYW
 * @date 2018/2/5
 */
public class AuthMarkingDTO implements Serializable {

    private String authRole;

    private String permission;

    public String getAuthRole() {
        return authRole;
    }

    public void setAuthRole(String authRole) {
        this.authRole = authRole;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
