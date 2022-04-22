package cn.v5cn.liyue.rpc.netty.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成ID
 * @author LiYue
 * Date: 2019/9/23
 */
public class RequestIdSupport {
    private final static AtomicInteger nextRequestId = new AtomicInteger(0);

    public static int next() {
        return nextRequestId.getAndIncrement();
    }
}
