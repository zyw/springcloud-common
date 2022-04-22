package cn.v5cn.cim.route.service;

import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.pojo.RouteInfo;
import cn.v5cn.cim.route.cache.ServerCache;
import cn.v5cn.cim.route.kit.NetAddressIsReachable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author crossoverJie
 */
@Component
public class CommonBizService {
    private static Logger logger = LoggerFactory.getLogger(CommonBizService.class) ;

    @Autowired
    private ServerCache serverCache ;

    /**
     * check ip and port
     * @param routeInfo
     */
    public void checkServerAvailable(RouteInfo routeInfo){
        boolean reachable = NetAddressIsReachable.checkAddressReachable(routeInfo.getIp(), routeInfo.getCimServerPort(), 1000);
        if (!reachable) {
            logger.error("ip={}, port={} are not available", routeInfo.getIp(), routeInfo.getCimServerPort());

            // rebuild cache
            serverCache.rebuildCacheList();

            throw new CIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }

    }
}
