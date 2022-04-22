package cn.v5cn.others.lru.lhm;

/**
 * 连表Element
 * @author ZYW
 */
public class Entry<K,V> {
    private Entry<K,V> pre;
    private Entry<K,V> next;
    private K key;
    private V value;

    public Entry<K, V> getPre() {
        return pre;
    }

    public void setPre(Entry<K, V> pre) {
        this.pre = pre;
    }

    public Entry<K, V> getNext() {
        return next;
    }

    public void setNext(Entry<K, V> next) {
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
