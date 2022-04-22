package cn.v5cn.netty.ws.pb.core.conn;

import com.google.inject.Singleton;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 使用内存存储连接
 * @author zyw
 */
@Singleton
public class MemoryConnContext<C extends Conn> implements ConnContext<C> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryConnContext.class);

    /**
     * 存储Server的连接
     * key:   netId
     * value: Conn
     */
    protected ConcurrentMap<Serializable, C> connMap;

    public MemoryConnContext() {
        this.connMap = new ConcurrentHashMap<>();
    }

    @Override
    public C getConn(ChannelHandlerContext ctx) {
        Serializable netId = ctx.channel().attr(Conn.NET_ID).get();
        if(netId == null) {
            LOGGER.warn("Conn netId not found in ctx, ctx: {}", ctx.toString());
            return null;
        }
        C conn = connMap.get(netId);
        if(conn == null) {
            LOGGER.warn("Conn not found, netId: {}", netId);
        }
        return conn;
    }

    @Override
    public C getConn(Serializable netId) {
        C conn = connMap.get(netId);
        if(conn == null) {
            LOGGER.warn("Conn not found, netId: {}", netId);
        }
        return conn;
    }

    @Override
    public void addConn(C conn) {
        LOGGER.debug("add a conn, netId: {}", conn.getNetId());
        connMap.put(conn.getNetId(), conn);
    }

    @Override
    public void removeConn(Serializable netId) {
        connMap.computeIfPresent(netId, (id, c) -> {
           c.close();
           return null;
        });
    }

    @Override
    public void removeConn(ChannelHandlerContext ctx) {
        Serializable netId = ctx.channel().attr(Conn.NET_ID).get();
        if(netId == null) {
            LOGGER.warn("Can't find a netId for the ctx");
        } else {
            removeConn(netId);
        }
    }

    @Override
    public void removeAllConn() {
        connMap.clear();
    }
}
