package cn.v5cn.rpc.lagou.transport;

import cn.v5cn.rpc.lagou.protocol.Message;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultPromise;

public class NettyResponseFuture<T> {
    private Long time;
    private Long timeOut;
    private Message<T> message;
    private Channel channel;
    private DefaultPromise promise;

    public NettyResponseFuture() {
    }

    public NettyResponseFuture(Long time, Long timeOut, Message<T> message, Channel channel, DefaultPromise promise) {
        this.time = time;
        this.timeOut = timeOut;
        this.message = message;
        this.channel = channel;
        this.promise = promise;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public Message<T> getMessage() {
        return message;
    }

    public void setMessage(Message<T> message) {
        this.message = message;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public DefaultPromise getPromise() {
        return promise;
    }

    public void setPromise(DefaultPromise promise) {
        this.promise = promise;
    }
}
