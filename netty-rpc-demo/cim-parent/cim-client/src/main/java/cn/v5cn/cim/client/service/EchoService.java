package cn.v5cn.cim.client.service;

/**
 * @author crossoverJie
 */
public interface EchoService {
    /**
     * echo msg to terminal
     * @param msg
     * @param replace
     */
    void echo(String msg, Object... replace);
}
