package cn.v5cn.cim.common.route.algorithm.consistenthash;

import cn.v5cn.cim.common.route.algorithm.RouteHandle;

import java.util.List;

/**
 * @author crossoverJie
 */
public class ConsistentHashHandle implements RouteHandle {
    private AbstractConsistentHash hash;

    public void setHash(AbstractConsistentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values, key);
    }
}
