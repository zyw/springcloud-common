package cn.v5cn.minio.demo.config;

import com.google.api.client.util.Lists;
import io.minio.MinioClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-12 16:46
 */
public class MinioClientList {

    private final List<MinioClient> CLIENTS;

    private static final AtomicInteger CURR_INDEX = new AtomicInteger(0);

    private static final ReentrantLock LOCK = new ReentrantLock();

    public MinioClientList(List<MinioClient> clients) {
        this.CLIENTS = clients;
    }

    public MinioClientList() {
        this.CLIENTS = Lists.newArrayList();
    }

    public void addClient(MinioClient client) {
        this.CLIENTS.add(client);
    }

    public MinioClient getClient() {
//        LOCK.lock();
//        int index = currIndex.getAndAdd(1);
//        if(index >= this.CLIENTS.size()) {
//            currIndex.set(0);
//            index = currIndex.getAndAdd(1);
//        }
//        LOCK.unlock();
//        return this.CLIENTS.get(index);
        LOCK.lock();
        //一个取模搞定轮训
        int count = CURR_INDEX.getAndIncrement(); //调用次数
        int size = this.CLIENTS.size();           //服务数量
        int i = count % size;                     //本次要使用的服务下标
        LOCK.unlock();
        return this.CLIENTS.get(i);
    }
}
