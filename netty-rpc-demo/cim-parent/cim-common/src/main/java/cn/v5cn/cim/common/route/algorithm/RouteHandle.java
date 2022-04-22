package cn.v5cn.cim.common.route.algorithm;

import java.util.List;

/**
 * @author crossoverJie
 */
public interface RouteHandle {
    /**
     * 再一批服务器里进行路由
     * @param values 路由信息
     * @param key key
     * @return 返回
     */
    String routeServer(List<String> values, String key);
}
