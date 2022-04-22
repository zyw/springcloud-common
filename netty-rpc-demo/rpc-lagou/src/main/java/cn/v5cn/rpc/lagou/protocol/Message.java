package cn.v5cn.rpc.lagou.protocol;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-09-10 22:36
 */
public class Message<T> {
    private Header header;
    private T payload;

    public Message(Header header, T payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public T getPayload() {
        return payload;
    }
}
