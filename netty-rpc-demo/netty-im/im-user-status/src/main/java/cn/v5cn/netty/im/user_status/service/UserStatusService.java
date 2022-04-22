package cn.v5cn.netty.im.user_status.service;

/**
 * @author yrw
 */
public interface UserStatusService {
    /**
     * user online
     *
     * @param userId
     * @param connectorId
     * @return the user's previous connection id, if don't exist then return null
     */
    String online(String userId, String connectorId);

    /**
     * user offline
     *
     * @param userId
     */
    void offline(String userId);

    /**
     * get connector id by user id
     * 用户ID和Connector关系
     *
     * @param userId
     * @return
     */
    String getConnectorId(String userId);
}
