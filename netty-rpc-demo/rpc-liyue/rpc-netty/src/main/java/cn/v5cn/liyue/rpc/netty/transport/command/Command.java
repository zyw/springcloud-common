package cn.v5cn.liyue.rpc.netty.transport.command;

/**
 * RPC 发送与响应的封装载体
 * @author LiYue
 * Date: 2019/9/20
 */
public class Command {
    protected Header header;
    private byte[] payload;

    public Command(Header header, byte [] payload) {
        this.header = header;
        this.payload = payload;
    }
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte [] getPayload() {
        return payload;
    }

    public void setPayload(byte [] payload) {
        this.payload = payload;
    }
}
