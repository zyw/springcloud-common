package cn.v5cn.cim.server.endpoint;

import cn.v5cn.cim.server.util.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

import java.util.Map;

/**
 * 自定义端点监控
 * @author crossoverJie
 */
public class CustomEndpoint extends AbstractEndpoint<Map<Long, NioSocketChannel>> {
    /**
     * 监控端点的 访问地址
     * @param id
     */
    public CustomEndpoint(String id) {
        //false 表示不是敏感端点
        super(id, false);
    }

    @Override
    public Map<Long, NioSocketChannel> invoke() {
        return SessionSocketHolder.getRelationShip();
    }
}
