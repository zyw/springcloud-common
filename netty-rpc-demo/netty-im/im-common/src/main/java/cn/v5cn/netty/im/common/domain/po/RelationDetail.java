package cn.v5cn.netty.im.common.domain.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author yrw
 */
@JsonIgnoreProperties({"deleted", "gmtUpdate"})
public class RelationDetail extends Relation {

    private String username1;

    private String username2;

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }
}
