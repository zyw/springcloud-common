package cn.v5cn.others.lru.linked;

import cn.v5cn.others.lru.LruCache;
import cn.v5cn.others.lru.Storage;

/**
 * @author ZYW
 */
public class LinkedHashMapLruCache<K,V> extends LruCache<K,V> {

    private LinkedHashMapCache<K,V> caching;

    public LinkedHashMapLruCache(int capacity, Storage<K,V> lowSpeedStorage) {
        super(capacity,lowSpeedStorage);
        caching = new LinkedHashMapCache<>(capacity);
    }

    @Override
    public V get(K key) {
        V result = caching.get(key);
        if(result == null) {
            result = lowSpeedStorage.get(key);
            caching.put(key,result);
        }
        return result;
    }
}
