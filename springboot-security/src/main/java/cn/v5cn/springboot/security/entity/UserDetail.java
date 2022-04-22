package cn.v5cn.springboot.security.entity;

import java.io.Serializable;
import java.time.Instant;

public class UserDetail implements Serializable {
    private Long accountId;
    private Long userId;
    private String userAccount;
    private String userPwd;
    private int status;
    private Instant lastPasswordResetDate;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Instant getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Instant lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }
}
