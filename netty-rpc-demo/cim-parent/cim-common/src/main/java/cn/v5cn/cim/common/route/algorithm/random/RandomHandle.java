package cn.v5cn.cim.common.route.algorithm.random;

import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.route.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 路由策略， 随机
 * @author jiangyunxiong
 */
public class RandomHandle implements RouteHandle {
    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if(size == 0) {
            throw new CIMException(StatusEnum.SERVER_NOT_AVAILABLE);
        }
        int offset = ThreadLocalRandom.current().nextInt(size);

        return values.get(offset);
    }
}
