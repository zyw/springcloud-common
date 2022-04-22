package cn.v5cn.others.lru;

/**
 * KV 存储抽象
 * @author LY
 */
public interface Storage<K,V> {
    /**
     * 根据提供的key来访问数据
     * @param key 数据key
     * @return 数据值
     */
    V get(K key);
}
