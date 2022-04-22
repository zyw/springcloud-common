package cn.v5cn.rpc.lagou.transport;

import java.util.HashMap;
import java.util.Map;

public class BeanManager {
    private static final Map<String,Object> BEAN_MAPS = new HashMap<>();

    public static Object getBean(String serviceName) {
        final Object o = BEAN_MAPS.get(serviceName);
        if(o == null) {
            throw new RuntimeException("bean 不存在");
        }
        return o;
    }

    public static void registerBean(String serviceName, Object service) {
        BEAN_MAPS.putIfAbsent(serviceName,service);
    }
}
