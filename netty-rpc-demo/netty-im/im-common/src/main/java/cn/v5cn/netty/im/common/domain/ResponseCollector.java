package cn.v5cn.netty.im.common.domain;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * thread safe
 * Date: 2019-05-18
 * Time: 13:50
 *
 * @author yrw
 */
public class ResponseCollector<M extends Message> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseCollector.class);

    private final Message sendMessage;
    private final Consumer<Message> sendFunction;
    private final CompletableFuture<M> future;

    private final AtomicLong sendTime;
    private final AtomicBoolean sending;

    public ResponseCollector(Message sendMessage, Consumer<Message> sendFunction) {
        this.sendMessage = sendMessage;
        this.sendFunction = sendFunction;
        this.future = new CompletableFuture<>();
        this.sendTime = new AtomicLong(0);
        this.sending = new AtomicBoolean(false);
    }

    public void send() {
        if(sending.compareAndSet(false, true)) {
            this.sendTime.set(System.nanoTime());
            try {
                sendFunction.accept(this.sendMessage);
            } catch (Exception e) {
                this.sending.set(false);
            }
        }
    }

    public long timeElapse() {
        return System.nanoTime() - sendTime.get();
    }

    public CompletableFuture<M> getFuture() {
        return this.future;
    }

    public AtomicLong getSendTime() {
        return this.sendTime;
    }

    public AtomicBoolean getSending() {
        return this.sending;
    }
}
