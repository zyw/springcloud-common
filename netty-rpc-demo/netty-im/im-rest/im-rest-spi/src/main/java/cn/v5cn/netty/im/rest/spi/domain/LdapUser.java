package cn.v5cn.netty.im.rest.spi.domain;

public class LdapUser extends UserBase {

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
