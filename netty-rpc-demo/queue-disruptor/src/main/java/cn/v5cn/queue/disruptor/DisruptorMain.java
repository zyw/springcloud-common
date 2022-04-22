package cn.v5cn.queue.disruptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 启动Disruptor
 * @author ZYW
 * @version 1.0
 * @date 2020-03-08 21:04
 */
public class DisruptorMain {

    private static Disruptor<LongEvent> disruptor = null;

    public static Disruptor<LongEvent> disruptor() {
        if(disruptor != null) {
            return disruptor;
        }
        //实例化事件工厂
        EventFactory<LongEvent> eventFactory = new LongEventFactory();

        //Consumer处理线程池
        //新版本只需要指定ThreadFactory
//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5,
//                200,
//                0L,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<Runnable>(1024),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy());

        // RingBuffer 大小，必须是 2 的 N 次方；
        int ringBufferSize = 1024;

//        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory,
//                ringBufferSize,
//                //老版本
//                poolExecutor,
//                ProducerType.SINGLE,
//                new SleepingWaitStrategy());
        //定义Disruptor组装各个组件
        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory,
                ringBufferSize,
                //新版本只需要指定ThreadFactory，Disruptor内部定义了一个BasicExecutor
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE,
                new SleepingWaitStrategy());

        //定义事件出列Handler
        EventHandler<LongEvent> eventHandler = new LongEventHandler();
        //设置事件处理handler，也就是在队列里有数据后调用的处理Handler
        disruptor.handleEventsWith(eventHandler);

        DisruptorMain.disruptor = disruptor;

        return DisruptorMain.disruptor;
    }

    public static void main(String[] args) throws InterruptedException {
        //启动队列
        Disruptor<LongEvent> disruptor = DisruptorMain.disruptor();
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        //LongEventProducer producer = new LongEventProducer(ringBuffer);
        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for(long l = 0; true; l++) {
            bb.putLong(0,l);
            producer.onData(bb);
            Thread.sleep(1000);
        }
    }
}
