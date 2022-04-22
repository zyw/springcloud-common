package cn.v5cn.rpc.jdk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RPC Server端
 *
 * @author ZYW
 * @version 1.0
 * @date 2020-02-27 16:07
 */
public class RPCServer {

    private static final ConcurrentHashMap<String,Class<?>> INTERFACE_IMPLS = new ConcurrentHashMap<>();

    //注册接口到服务
    static {
        INTERFACE_IMPLS.put(SayHello.class.getName(),SayHelloImpl.class);
    }

    // 通过ThreadPoolExecutor创建线程池
    private final static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(5,
            200,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024),
            new ThreadFactory() {
                AtomicInteger seq = new AtomicInteger();

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("rpc-" + seq.getAndIncrement());
                    return t;
                }
            }, new ThreadPoolExecutor.AbortPolicy());
    // 也可以使用下面的方式
//    private final static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(5,
//            200,
//            0L,
//            TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<Runnable>(1024),
//            Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

    public RPCServer() {

    }

    public static void exporter(String hostname, int port) throws Exception {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(hostname, port));
        try {
            while (true) {
                EXECUTOR.execute(new ExporterTask(server.accept()));
            }
        } finally {

        }
    }

    private static class ExporterTask implements Runnable {

        private Socket socket;

        public ExporterTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //使用JDK自带的序列化
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try {
                input = new ObjectInputStream(socket.getInputStream());

                String interfaceName = input.readUTF();
                Class<?> service = INTERFACE_IMPLS.get(interfaceName);

                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();

                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);

                //创建输出流
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null) {
                        output.close();
                    }
                    if (input != null) {
                        input.close();
                    }
                    if(socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
