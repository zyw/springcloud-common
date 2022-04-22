package cn.v5cn.liyue.rpc.server;

import cn.v5cn.liyue.rpc.hello.HelloService2;
import cn.v5cn.liyue.rpc.hello.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloService2Impl implements HelloService2 {

    private static final Logger logger = LoggerFactory.getLogger(HelloService2Impl.class);

    @Override
    public String hello(Person person,String type) {
        logger.info("HelloService2Impl-------------------------------{}",type);
        logger.info(person.toString());
        logger.info("HelloService2Impl-------------------------------");
        return person.toString();
    }
}
