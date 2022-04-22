package cn.v5cn.others.lru.linked;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 使用LinkedHashMap实现LRU(最近最少使用)
 * @author ZYW
 */
public class LinkedHashMapCache<K,V> extends LinkedHashMap<K,V> {

    private int capacity;

    public LinkedHashMapCache(int capacity){
        /**
         * accessOrder=true表示按照最近最新使用的排序
         */
        super((int) Math.ceil(capacity / 0.75) + 1,0.75f,true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        //当大小查过传入的乏值时，将按照最近最少使用淘汰数据（LRU）
        return size() > capacity;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<K, V> entry : entrySet()) {
            sb.append(String.format("%s:%s ", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}
