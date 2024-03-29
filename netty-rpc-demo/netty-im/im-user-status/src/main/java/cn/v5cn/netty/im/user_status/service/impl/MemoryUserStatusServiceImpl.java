package cn.v5cn.netty.im.user_status.service.impl;

import cn.v5cn.netty.im.user_status.service.UserStatusService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * it's for test
 * Date: 2019-09-02
 * Time: 20:33
 *
 * @author yrw
 */
public class MemoryUserStatusServiceImpl implements UserStatusService {

    private ConcurrentMap<String, String> userIdConnectorIdMap;

    public MemoryUserStatusServiceImpl() {
        this.userIdConnectorIdMap = new ConcurrentHashMap<>();
    }

    @Override
    public String online(String userId, String connectorId) {
        return userIdConnectorIdMap.put(userId, connectorId);
    }

    @Override
    public void offline(String userId) {
        userIdConnectorIdMap.remove(userId);
    }

    @Override
    public String getConnectorId(String userId) {
        return userIdConnectorIdMap.get(userId);
    }
}
