package cn.v5cn.simple.rpc.demo;

/**
 * 指数RPC的输出
 * @author ZYW
 * @version 1.0
 * @date 2020-02-25 15:15
 */
public class ExpResponse {
    private long value;
    private long costInNanos;

    public ExpResponse() {
    }

    public ExpResponse(long value, long costInNanos) {
        this.value = value;
        this.costInNanos = costInNanos;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getCostInNanos() {
        return costInNanos;
    }

    public void setCostInNanos(long costInNanos) {
        this.costInNanos = costInNanos;
    }
}
