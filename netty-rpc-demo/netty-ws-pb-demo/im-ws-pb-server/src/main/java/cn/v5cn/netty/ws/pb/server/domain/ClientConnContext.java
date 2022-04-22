package cn.v5cn.netty.ws.pb.server.domain;

import cn.v5cn.netty.ws.pb.core.conn.MemoryConnContext;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 保存客户端连接容器
 * @author zyw
 */
@Singleton
public class ClientConnContext extends MemoryConnContext<ClientConn> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConnContext.class);

    /**
     * key:   UserId
     * value: NetId
     */
    private final ConcurrentMap<String, Serializable> userIdToNetId;

    public ClientConnContext() {
        this.userIdToNetId = new ConcurrentHashMap<>();
        this.connMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addConn(ClientConn conn) {
        String userId = conn.getUserId();
        if(userIdToNetId.containsKey(userId)) {
            removeConn(userIdToNetId.get(userId));
        }

        LOGGER.debug("[add conn on this machine] user: {} is online, netId", userId, conn.getNetId());

        connMap.putIfAbsent(conn.getNetId(), conn);
        userIdToNetId.put(conn.getUserId(),conn.getNetId());
    }

    public ClientConn getConnByUserId(String userId) {
        LOGGER.debug("[get conn on this machine] userId: {}", userId);

        Serializable netId = userIdToNetId.get(userId);
        if(netId == null) {
            LOGGER.debug("[get conn this machine] netId not found");
            return null;
        }

        ClientConn conn = connMap.get(netId);
        if(conn == null) {
            LOGGER.debug("[get conn this machine] conn not found");
            userIdToNetId.remove(userId);
        } else {
            LOGGER.debug("[get conn this machine] found conn, userId:{}, connId: {}", userId, conn.getNetId());
        }

        return conn;
    }

    public List<ClientConn> getConnListByGroupId(String groupId) {
        LOGGER.debug("[get conn on this machine] groupId: {}", groupId);
        // TODO 暂时没有用到groupId
        return userIdToNetId.values().stream().map(item -> connMap.get(item)).collect(Collectors.toList());
    }
}
