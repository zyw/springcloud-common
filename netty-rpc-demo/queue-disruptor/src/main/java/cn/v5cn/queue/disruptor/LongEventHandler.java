package cn.v5cn.queue.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * 定义事件处理的具体实现
 * 通过实现接口 com.lmax.disruptor.EventHandler<T> 定义事件处理的具体实现。
 * @author ZYW
 * @version 1.0
 * @date 2020-03-08 20:49
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        System.out.println("Event: " + longEvent);
    }
}
