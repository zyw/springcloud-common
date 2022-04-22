package cn.v5cn.rpc.lagou.transport;

import cn.v5cn.rpc.lagou.protocol.Message;
import cn.v5cn.rpc.lagou.protocol.Request;
import cn.v5cn.rpc.lagou.protocol.Response;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

public class InvokeRunnable implements Runnable {

    private ChannelHandlerContext ctx;
    private Message<Request> message;

    public InvokeRunnable(ChannelHandlerContext ctx, Message<Request> message) {
        this.ctx = ctx;
        this.message = message;
    }

    @Override
    public void run() {
        Response response = new Response();
        Object result = null;
        try {
            final Request request = message.getPayload();
            final String serviceName = request.getServiceName();
            // 这里提供BeanManager对所有业务Bean进行管理，其底层在内存中维护了
            // 一个业务Bean实例的集合。感兴趣的同学可以尝试接入Spring等容器管
            // 理业务Bean
            Object bean = BeanManager.getBean(serviceName);
            // 下面通过反射调用Bean中的相应方法
            Method method = bean.getClass().getMethod(request.getMethodName(), request.getArgTypes());
            result = method.invoke(bean, request.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        response.setResult(result);// 设置响应结果
        // 将响应消息返回给客户端
        ctx.writeAndFlush(new Message<>(message.getHeader(), response));
    }
}
