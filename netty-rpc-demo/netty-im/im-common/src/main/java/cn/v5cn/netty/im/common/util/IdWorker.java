package cn.v5cn.netty.im.common.util;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ID生成器
 * @author yrw
 */
public class IdWorker {
    private static final SnowFlake SNOW_FLAKE;
    private static final ConcurrentMap<Serializable, AtomicLong> SESSION_MAP;

    static {
        SNOW_FLAKE = new SnowFlake(1, 1);
        SESSION_MAP = new ConcurrentHashMap<>();
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * snowFlake
     * for InternalMsg
     *
     * @return
     */
    public static Long snowGenId() {
        return SNOW_FLAKE.nextId();
    }

    /**
     * consistent id
     * for ChatMsg, AckMsg
     *
     * @return
     */
    public static Long nextId(Serializable connectorId) {
        return SESSION_MAP.computeIfAbsent(connectorId,
                key -> new AtomicLong(0)).incrementAndGet();
    }
}
