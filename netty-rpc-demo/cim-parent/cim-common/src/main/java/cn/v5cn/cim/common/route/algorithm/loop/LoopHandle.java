package cn.v5cn.cim.common.route.algorithm.loop;

import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.route.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 循环
 * @author crossoverJie
 */
public class LoopHandle implements RouteHandle {
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values, String key) {
        if (values.size() == 0) {
            throw new CIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        long position = index.incrementAndGet() % values.size();
        if(position < 0) {
            position = 0L;
        }
        return values.get(Long.valueOf(position).intValue());
    }
}
