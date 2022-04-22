package cn.v5cn.netty.ws.pb.server.status.impl;

import cn.v5cn.netty.ws.pb.server.status.UserStatusService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zyw
 */
public class MemoryUserStatusServiceImpl implements UserStatusService {

    private final ConcurrentMap<String, String> userIdServerIdMap;

    public MemoryUserStatusServiceImpl() {
        this.userIdServerIdMap = new ConcurrentHashMap<>();
    }

    @Override
    public String online(String userId, String connectorId) {
        return userIdServerIdMap.put(userId, connectorId);
    }

    @Override
    public void offline(String userId) {
        userIdServerIdMap.remove(userId);
    }

    @Override
    public String getConnectorId(String userId) {
        return userIdServerIdMap.get(userId);
    }
}
