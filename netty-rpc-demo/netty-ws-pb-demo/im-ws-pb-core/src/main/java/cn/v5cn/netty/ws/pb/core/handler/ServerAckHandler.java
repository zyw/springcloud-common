package cn.v5cn.netty.ws.pb.core.handler;

import cn.v5cn.netty.ws.pb.core.entity.InternalMsg;
import cn.v5cn.netty.ws.pb.core.exception.ImLogicException;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 对于服务器，每个连接都应该有一个ServerAckWindow
 * @author zyw
 */
public class ServerAckHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerAckHandler.class);

    private final static Map<Serializable, ServerAckHandler> HANDLER_MAP;

    private final static ExecutorService executorService;

    private final Duration timeout;
    private final int maxSize;

    private final ConcurrentHashMap<Long, ResponseServer<InternalMsg>> responseServerMap;

    static {
        HANDLER_MAP = new ConcurrentHashMap<>();
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(ServerAckHandler::checkTimeoutAndRetry);
    }

    public ServerAckHandler(Serializable connectionId, int maxSize, Duration timeout) {
        this.responseServerMap = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.timeout = timeout;

        HANDLER_MAP.put(connectionId, this);
    }

    /**
     * 多线程操作
     * @param connectionId 连接ID
     * @param id msg id
     * @param sendMessage 发送消息
     * @param sendFunction 发送函数
     * @return 返回
     */
    public static CompletableFuture<InternalMsg> offer(Serializable connectionId, Long id, Message sendMessage, Consumer<Message> sendFunction) {
        return HANDLER_MAP.get(connectionId).offer(id, sendMessage, sendFunction);
    }

    /**
     * 多线程操作
     * @param id msg id
     * @param sendMessage 发送消息
     * @param sendFunction 发送函数
     * @return 返回
     */
    public CompletableFuture<InternalMsg> offer(Long id, Message sendMessage, Consumer<Message> sendFunction) {
        if(responseServerMap.containsKey(id)) {
            CompletableFuture<InternalMsg> future = new CompletableFuture<>();
            future.completeExceptionally(new ImLogicException("send repeat msg id: " + id));
            return future;
        }

        if(responseServerMap.size() >= maxSize) {
            CompletableFuture<InternalMsg> future = new CompletableFuture<>();
            future.completeExceptionally(new ImLogicException("server window is full"));
        }

        ResponseServer<InternalMsg> responseServer = new ResponseServer<>(sendMessage,sendFunction);
        responseServer.send();
        responseServerMap.put(id, responseServer);
        return responseServer.getFuture();
    }

    public void ack(InternalMsg msg) {
        long id = Long.parseLong(msg.getMsgBody());
        LOGGER.debug("get ack, msg: {}", id);
        if(responseServerMap.containsKey(id)) {
            responseServerMap.get(id).getFuture().complete(msg);
            responseServerMap.remove(id);
        }
    }

    /**
     * single thread do it
     */
    private static void checkTimeoutAndRetry() {
        while (true) {
            for(ServerAckHandler handler : HANDLER_MAP.values()) {
                handler.responseServerMap.entrySet().stream()
                        .filter(entry -> handler.timeout(entry.getValue()))
                        .forEach(entry -> handler.retry(entry.getKey(), entry.getValue()));
            }
        }
    }

    private void retry(Long id, ResponseServer<?> server) {
        LOGGER.debug("retry msg: {} ", id);
        // todo: if offline
        server.send();
    }

    private boolean timeout(ResponseServer<?> server) {
        return server.getSendTime().get() != 0 && server.timeElapse() > timeout.toNanos();
    }
}
