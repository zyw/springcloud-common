package cn.v5cn.simple.rpc.demo;

import cn.v5cn.simple.rpc.common.IMessageHandler;
import cn.v5cn.simple.rpc.common.MessageOutput;
import cn.v5cn.simple.rpc.server.RPCServer;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 15:31
 */
// 斐波那系数处理类
class FibRequestHandler implements IMessageHandler<Integer> {

    private List<Long> fibs = new ArrayList<>();

    {
        fibs.add(1L); // fib(0) = 1
        fibs.add(1L); // fib(1) = 1
    }

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, Integer n) {
        for(int i = fibs.size(); i < n + 1; i++) {
            long value = fibs.get(i - 2) + fibs.get(i -1);
            fibs.add(value);
        }
        ctx.writeAndFlush(new MessageOutput(requestId,"fib_res",fibs.get(n)));
    }
}
// 指数处理类
class ExpRequestHandler implements IMessageHandler<ExpRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, ExpRequest message) {
        int base = message.getBase();
        int exp = message.getExp();
        long start = System.nanoTime();
        long res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        long cost = System.nanoTime() - start;
        ctx.writeAndFlush(new MessageOutput(requestId, "exp_res", new ExpResponse(res, cost)));
    }
}

public class DemoServer {
    public static void main(String[] args) {
        RPCServer server = new RPCServer("localhost",8088,2,16);
        //注册两个处理类和要处理的类型
        server.service("fib",Integer.class,new FibRequestHandler())
                .service("exp",ExpRequest.class,new ExpRequestHandler());
        server.start();
    }
}
