package cn.v5cn.netty.im.connector.domain;

import cn.v5cn.netty.im.common.domain.conn.MemoryConnContext;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 保存客户端连接容器
 * Date: 2019-05-02
 * Time: 07:55
 *
 * @author yrw
 */
@Singleton
public class ClientConnContext extends MemoryConnContext<ClientConn> {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnContext.class);

    private final ConcurrentMap<String, Serializable> userIdToNetId;

    public ClientConnContext() {
        this.connMap = new ConcurrentHashMap<>();
        this.userIdToNetId = new ConcurrentHashMap<>();
    }

    public ClientConn getConnByUserId(String userId) {
        logger.debug("[get conn on this machine] userId: {}", userId);

        final Serializable netId = userIdToNetId.get(userId);
        if(netId == null) {
            logger.debug("[get conn this machine] netId not found");
            return null;
        }

        final ClientConn conn = connMap.get(netId);
        if(conn == null) {
            logger.debug("[get conn this machine] conn not found");
            userIdToNetId.remove(userId);
        } else {
            logger.debug("[get conn this machine] found conn, userId:{}, connId: {}", userId, conn.getNetId());
        }
        return conn;
    }

    @Override
    public void addConn(ClientConn conn) {
        final String userId = conn.getUserId();

        if(userIdToNetId.containsKey(userId)) {
            removeConn(userIdToNetId.get(userId));
        }
        logger.debug("[add conn on this machine] user: {} is online, netId", userId, conn.getNetId());

        connMap.putIfAbsent(conn.getNetId(), conn);
        userIdToNetId.put(conn.getUserId(), conn.getNetId());
    }
}
