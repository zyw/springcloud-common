package cn.v5cn.simple.rpc.common;

import java.util.UUID;

/**
 * 请求ID生成器，简单的UUID64
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 22:30
 */
public class RequestId {
    public static String next() {
        return UUID.randomUUID().toString();
    }
}
