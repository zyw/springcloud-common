package cn.v5cn.liyue.rpc.client;

import cn.v5cn.liyue.rpc.api.NameService;
import cn.v5cn.liyue.rpc.api.RpcAccessPoint;
import cn.v5cn.liyue.rpc.api.spi.ServiceSupport;
import cn.v5cn.liyue.rpc.hello.HelloService;
import cn.v5cn.liyue.rpc.hello.HelloService2;
import cn.v5cn.liyue.rpc.hello.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws Exception {
//        final String serviceName = HelloService.class.getCanonicalName();
        final String serviceName2 = HelloService2.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        String name = "Master MQ";
        Person person = new Person("张三",20);
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)){
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;
//            URI uri = nameService.lookupService(serviceName);
            URI uri2 = nameService.lookupService(serviceName2);
            assert uri2 != null;
            logger.info("找到服务{}，提供者: {}.", serviceName2, uri2);
//            HelloService helloService = rpcAccessPoint.getRemoteService(uri,HelloService.class);
            HelloService2 helloService2 = rpcAccessPoint.getRemoteService(uri2,HelloService2.class);
//            String response = helloService.hello(name);
            final String hello = helloService2.hello(person,"Hello");
            logger.info("收到响应: {}. 响应2：{}", "response",hello);
        }
    }
}
