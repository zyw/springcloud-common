package cn.v5cn.cim.server.util;

import cn.v5cn.cim.common.pojo.CIMUserInfo;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author crossoverJie
 */
public class SessionSocketHolder {
    private static final Map<Long, NioSocketChannel> CHANNEL_MAP = new ConcurrentHashMap<>(16);
    private static final Map<Long, String> SESSION_MAP = new ConcurrentHashMap<>(16);

    public static void saveSession(Long userId, String userName) {
        SESSION_MAP.put(userId, userName);
    }

    public static void removeSession(Long userId) {
        SESSION_MAP.remove(userId);
    }

    /**
     * Save the relationship between the userId and the channel.
     * 保存 userId 和 channel 的关系。
     * @param id userId
     * @param socketChannel channel
     */
    public static void put(Long id, NioSocketChannel socketChannel) {
        CHANNEL_MAP.put(id, socketChannel);
    }

    public static NioSocketChannel get(Long id) {
        return CHANNEL_MAP.get(id);
    }

    public static Map<Long, NioSocketChannel> getRelationShip() {
        return CHANNEL_MAP;
    }

    public static void remove(NioSocketChannel nioSocketChannel) {
        CHANNEL_MAP.entrySet().stream().filter(entry -> entry.getValue() == nioSocketChannel).forEach(entry -> CHANNEL_MAP.remove(entry.getKey()));
    }

    /**
     * 获取注册用户信息
     * @param nioSocketChannel NioSocket通道
     * @return 返回用户信息
     */
    public static CIMUserInfo getUserId(NioSocketChannel nioSocketChannel) {
        for (Map.Entry<Long, NioSocketChannel> entry : CHANNEL_MAP.entrySet()) {
            NioSocketChannel value = entry.getValue();
            if(nioSocketChannel == value) {
                Long key = entry.getKey();
                String userName = SESSION_MAP.get(key);
                return new CIMUserInfo(key, userName);
            }
        }
        return null;
    }
}
