package cn.v5cn.rpc.lagou.registry;

import java.io.Serializable;

public class ServerInfo implements Serializable {
    private String host;
    private int port;
    private String describe;

    public ServerInfo() { }

    public ServerInfo(String host, int port, String describe) {
        this.host = host;
        this.port = port;
        this.describe = describe;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
