package cn.v5cn.netty.im.client.context;

import cn.v5cn.netty.im.client.handler.ClientConnectorHandler;
import cn.v5cn.netty.im.common.domain.po.Relation;
import cn.v5cn.netty.im.common.domain.po.RelationDetail;
import com.google.inject.Inject;

import java.util.List;

public class UserContext {

    private String userId;

    private String token;

    private RelationCache relationCache;

    private ClientConnectorHandler clientConnectorHandler;

    @Inject
    public UserContext(RelationCache relationCache) {
        this.relationCache = relationCache;
    }

    public ClientConnectorHandler getClientConnectorHandler() {
        return clientConnectorHandler;
    }

    public void setClientConnectorHandler(ClientConnectorHandler clientConnectorHandler) {
        this.clientConnectorHandler = clientConnectorHandler;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RelationCache getRelationCache() {
        return relationCache;
    }

    public void setRelationCache(RelationCache relationCache) {
        this.relationCache = relationCache;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addRelations(List<RelationDetail> relations) {
        relationCache.addRelations(relations);
    }

    public void addRelation(RelationDetail relation) {
        relationCache.addRelation(relation);
    }

    public Relation getRelation(String userId1, String userId2) {
        return relationCache.getRelation(userId1, userId2, token);
    }
}
