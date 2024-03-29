package cn.v5cn.netty.ws.pb.core.handler;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author zyw
 */
public class ResponseServer<M extends Message> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseServer.class);

    private final Message sendMessage;
    private final Consumer<Message> sendFunction;
    private final CompletableFuture<M> future;

    private final AtomicLong sendTime;
    private final AtomicBoolean sending;

    public ResponseServer(Message sendMessage,Consumer<Message> sendFunction) {
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
