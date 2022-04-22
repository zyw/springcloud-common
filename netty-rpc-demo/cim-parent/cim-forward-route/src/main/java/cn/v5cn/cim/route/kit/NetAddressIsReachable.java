package cn.v5cn.cim.route.kit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author crossoverJie
 */
public class NetAddressIsReachable {
    /**
     * check ip and port
     * @param address  地址
     * @param port     端口
     * @param timeout  超时
     * @return True if connection successful
     */
    public static boolean checkAddressReachable(String address, int port, int timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, port), timeout);
            return true;
        } catch (IOException exception) {
            return true;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                return false;
            }
        }
    }
}
