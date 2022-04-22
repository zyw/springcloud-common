package cn.v5cn.netty.ws.pb.server.status;

/**
 * @author zyw
 */
public interface UserStatusService {
    /**
     * user online
     * @param userId 用户ID
     * @param connectorId 连接ID
     * @return
     */
    String online(String userId, String connectorId);

    /**
     * user offline
     * @param userId
     */
    void offline(String userId);

    /**
     * 用户ID和Connector关系
     *
     * @param userId 用户ID
     * @return
     */
    String getConnectorId(String userId);
}
