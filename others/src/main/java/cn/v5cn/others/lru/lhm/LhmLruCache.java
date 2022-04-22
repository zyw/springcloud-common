package cn.v5cn.others.lru.lhm;

import cn.v5cn.others.lru.LruCache;
import cn.v5cn.others.lru.Storage;

/**
 * @author ZYW
 */
public class LhmLruCache<K,V> extends LruCache<K,V> {

    private LhmCache<K,V> caching;

    public LhmLruCache(int capacity, Storage<K,V> lowSpeedStorage) {
        super(capacity, lowSpeedStorage);
        caching = new LhmCache<>(capacity);
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
