package cn.v5cn.liyue.rpc.server;

import cn.v5cn.liyue.rpc.api.NameService;
import cn.v5cn.liyue.rpc.api.RpcAccessPoint;
import cn.v5cn.liyue.rpc.api.spi.ServiceSupport;
import cn.v5cn.liyue.rpc.hello.HelloService;
import cn.v5cn.liyue.rpc.hello.HelloService2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        String serviceName = HelloService.class.getCanonicalName();
        String serviceName2 = HelloService2.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        HelloService helloService = new HelloServiceImpl();
        HelloService2 helloService2 = new HelloService2Impl();
        logger.info("创建并启动RpcAccessPoint...");
        try (
                RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
                Closeable ignored = rpcAccessPoint.startServer()
        ){
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;
            logger.info("向RpcAccessPoint注册{}服务...", serviceName);
            URI uri = rpcAccessPoint.addServiceProvider(helloService,HelloService.class);
            // 添加服务2
            URI uri2 = rpcAccessPoint.addServiceProvider(helloService2,HelloService2.class);
            logger.info("服务名: {}, 向NameService注册...", serviceName2);
            nameService.registerService(serviceName,uri);
            nameService.registerService(serviceName2,uri2);
            logger.info("开始提供服务，按任何键退出.");

            System.in.read();
            logger.info("Bye!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
