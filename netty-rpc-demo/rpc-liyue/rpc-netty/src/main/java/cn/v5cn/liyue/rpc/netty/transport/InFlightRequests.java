package cn.v5cn.liyue.rpc.netty.transport;

import java.io.Closeable;
import java.util.Map;
import java.util.concurrent.*;

/**
 * inFlightRequests 中存放了所有在途的请求，也就是已经发出了请求但还没有收到响应的这些 responseFuture 对象
 * @author LiYue
 * Date: 2019/9/20
 */
public class InFlightRequests implements Closeable {

    private final static long TIMEOUT_SEC = 10L;

    private final Semaphore semaphore = new Semaphore(10);
    private final Map<Integer,ResponseFuture> futureMap = new ConcurrentHashMap<>();
    // 创建定时器
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledFuture scheduledFuture;

    public InFlightRequests(){
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures, TIMEOUT_SEC, TIMEOUT_SEC, TimeUnit.SECONDS);
    }

    public void put(ResponseFuture responseFuture) throws InterruptedException, TimeoutException {
        if(semaphore.tryAcquire(TIMEOUT_SEC,TimeUnit.SECONDS)) {
            futureMap.put(responseFuture.getRequestId(),responseFuture);
        } else {
            throw new TimeoutException();
        }
    }

    private void removeTimeoutFutures() {
        futureMap.entrySet().removeIf(entry -> {
            if(System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000L) {
                semaphore.release();
                return true;
            } else {
                return false;
            }
        });
    }

    public ResponseFuture remove(int requestId) {
        ResponseFuture future = futureMap.remove(requestId);
        if(null != future) {
            semaphore.release();
        }
        return future;
    }

    @Override
    public void close() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}
