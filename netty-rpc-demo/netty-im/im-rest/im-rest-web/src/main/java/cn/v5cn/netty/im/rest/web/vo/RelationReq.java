package cn.v5cn.netty.im.rest.web.vo;

import javax.validation.constraints.NotEmpty;

public class RelationReq {
    @NotEmpty
    private String userId1;

    @NotEmpty
    private String userId2;

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }
}
