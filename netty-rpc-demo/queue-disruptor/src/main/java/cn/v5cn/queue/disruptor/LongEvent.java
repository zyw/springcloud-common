package cn.v5cn.queue.disruptor;

/**
 * 事件(Event)就是通过 Disruptor 进行交换的数据类型。（发布到队列中的数据类型，消费者可以消费的数据类型）
 * @author ZYW
 * @version 1.0
 * @date 2020-03-08 20:25
 */
public class LongEvent {
    private Long value;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EventLong{" +
                "value=" + value +
                '}';
    }
}
