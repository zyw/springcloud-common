package cn.v5cn.cim.common.pojo;

import java.util.Objects;

/**
 * @author crossoverJie
 */
public class CIMUserInfo {

    private Long userId;
    private String userName;

    public CIMUserInfo(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CIMUserInfo that = (CIMUserInfo) o;
        return Objects.equals(userId, that.userId) && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName);
    }

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
        return "CIMUserInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
