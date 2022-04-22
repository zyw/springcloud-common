package cn.v5cn.netty.im.client.context;

import cn.v5cn.netty.im.common.domain.po.RelationDetail;

import java.util.List;

public interface RelationCache {
    /**
     * add multiple relations
     *
     * @param relations
     */
    void addRelations(List<RelationDetail> relations);

    /**
     * add a relation
     *
     * @param relation
     */
    void addRelation(RelationDetail relation);

    /**
     * get relation by userId
     *
     * @param userId1
     * @param userId2
     * @param token
     * @return
     */
    RelationDetail getRelation(String userId1, String userId2, String token);
}
