package cn.v5cn.cim.common.util;

import cn.v5cn.cim.common.enums.StatusEnum;
import cn.v5cn.cim.common.exception.CIMException;
import cn.v5cn.cim.common.pojo.RouteInfo;

/**
 * @author crossoverJie
 */
public class RouteInfoParseUtil {

    public static RouteInfo parse(String info) {
        try {
            String[] serverInfo = info.split(":");
            return new RouteInfo(serverInfo[0], Integer.parseInt(serverInfo[1]), Integer.parseInt(serverInfo[2]));
        } catch (Exception e) {
            throw new CIMException(StatusEnum.VALIDATION_FAIL);
        }
    }
}
