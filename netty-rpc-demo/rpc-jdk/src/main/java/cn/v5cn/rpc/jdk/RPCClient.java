package cn.v5cn.rpc.jdk;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-27 17:07
 */
public class RPCClient<S> implements InvocationHandler {

    private Class<?> serviceClass;
    private InetSocketAddress addr;

    public S importer(final Class<?> serviceClass, final InetSocketAddress addr) {
        this.serviceClass = serviceClass;
        this.addr = addr;
        return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class<?>[]{serviceClass},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;
        try {
            socket = new Socket();
            socket.connect(addr);
            output = new ObjectOutputStream(socket.getOutputStream());
            output.writeUTF(serviceClass.getName());
            output.writeUTF(method.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(args);
            input = new ObjectInputStream(socket.getInputStream());
            return input.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(socket != null) {
                socket.close();
            }
            if(output != null) {
                output.close();
            }
            if(input != null) {
                input.close();
            }
        }
        return null;
    }
}
