package cn.v5cn.liyue.rpc.netty.transport;

import cn.v5cn.liyue.rpc.netty.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 发送请求接口
 * @author LiYue
 * Date: 2019/9/20
 */
public interface Transport {
    /**
     * 发送请求命令
     * @param request 请求命令
     * @return 返回值是一个Future，Future
     */
    CompletableFuture<Command> send(Command request);
}
