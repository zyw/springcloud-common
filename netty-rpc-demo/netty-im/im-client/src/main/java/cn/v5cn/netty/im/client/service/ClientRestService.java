package cn.v5cn.netty.im.client.service;

import cn.v5cn.netty.im.client.domain.UserReq;
import cn.v5cn.netty.im.common.domain.UserInfo;
import cn.v5cn.netty.im.common.domain.po.RelationDetail;
import cn.v5cn.netty.im.common.rest.AbstractRestService;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.List;

/**
 * @author yrw
 */
public class ClientRestService extends AbstractRestService<ClientRestClient> {

    @Inject
    public ClientRestService(@Assisted String url) {
        super(ClientRestClient.class, url);
    }

    public UserInfo login(String username, String pwd) {
        return doRequest(() -> restClient.login(new UserReq(username, pwd)).execute());
    }

    public Void logout(String token) {
        return doRequest(() -> restClient.logout(token).execute());
    }

    public List<RelationDetail> friends(String userId, String token) {
        return doRequest(() -> restClient.friends(userId, token).execute());
    }

    public RelationDetail relation(String userId, String userId2, String token) {
        return doRequest(() -> restClient.relation(userId,userId2,token).execute());
    }
}
