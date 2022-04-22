package cn.v5cn.jdk.spi.demo1;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * <p>
 *     服务获取类，用于获取实现服务定义接口的服务。
 *     通常跟服务接口在一个jar文件中定义
 * </p>
 * @author ZYW
 * @version 1.0
 * @date 2020-02-29 15:00
 */
public class ServiceObtain {
    public List<SayHello> services() {
        //通过ServiceLoader发现服务提供者
        ServiceLoader<SayHello> hellos = ServiceLoader.load(SayHello.class);
        return StreamSupport.stream(hellos.spliterator(), false).collect(Collectors.toList());
    }
}
