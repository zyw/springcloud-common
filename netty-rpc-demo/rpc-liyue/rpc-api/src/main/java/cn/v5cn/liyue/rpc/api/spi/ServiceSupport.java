package cn.v5cn.liyue.rpc.api.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * SPI类加载器帮助类
 * @author LiYue
 * Date: 2019-03-26
 */
public class ServiceSupport {

    private final static Map<String,Object> singletonServices = new HashMap<>();

    /**
     * 加载单个
     * @param service
     * @param <S>
     * @return
     */
    public synchronized static <S> S load(Class<S> service) {
        return StreamSupport
                .stream(ServiceLoader.load(service).spliterator(),false)
                .map(ServiceSupport::singletonFilter)
                .findFirst().orElseThrow(ServiceLoadException::new);
    }

    /**
     * 加载全部
     * @param service
     * @param <S>
     * @return
     */
    public synchronized static <S> Collection<S> loadAll(Class<S> service) {
        return StreamSupport.
                stream(ServiceLoader.load(service).spliterator(), false)
                .map(ServiceSupport::singletonFilter).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <S> S singletonFilter(S service) {
        if(service.getClass().isAnnotationPresent(Singleton.class)) {
            String className = service.getClass().getCanonicalName();
            Object singletonInstance = singletonServices.putIfAbsent(className, service);
            return singletonInstance == null ? service : (S)singletonInstance;
        } else {
            return service;
        }
    }

}
