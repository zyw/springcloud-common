package cn.v5cn.others.lru.lhm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LHM->linked+HashMap连表+HashMap实现
 * @author ZYW
 */
public class LhmCache<K,V> {
    private final int MAX_CACHE_SIZE;

    private Entry<K,V> first;
    private Entry<K,V> last;

    private Map<K,Entry<K,V>> data;

    public LhmCache(int cacheSize) {
        this.MAX_CACHE_SIZE = cacheSize;
        data = new ConcurrentHashMap<>();
    }

    public void put(K key,V value) {
        Entry<K, V> entry = getEntry(key);
        if(entry == null) {
            if(data.size() >= MAX_CACHE_SIZE) {
                data.remove(last.getKey());
                removeLast();
            }
            entry = new Entry<>();
            entry.setKey(key);
        }
        entry.setValue(value);
        moveToFirst(entry);
        data.put(key,entry);
    }

    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        if(entry == null) {
            return null;
        }
        moveToFirst(entry);
        return entry.getValue();
    }

    private void remove(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry !=null) {
            if(entry.getPre() != null) {
                entry.getPre().setNext(entry.getNext());
            }
            if(entry.getNext() != null) {
                entry.getNext().setPre(entry.getPre());
            }
            if(entry == first) {
                first = entry.getNext();
            }
            if(entry == last) {
                last = entry.getPre();
            }
        }
        data.remove(key);
    }

    private void moveToFirst(Entry<K,V> entry) {
        if (entry == first) {
            return;
        }
        if (entry.getPre() != null) {
            entry.getPre().setNext(entry.getNext());
        }
        if(entry.getNext() != null) {
            entry.getNext().setPre(entry.getPre());
        }
        if (entry == last) {
            last = last.getPre();
        }

        if(first == null || last == null) {
            first = last = entry;
            return;
        }

        entry.setNext(first);
        first.setPre(entry);
        entry.setPre(null);
    }

    private void removeLast(){
        if(last != null) {
            last = last.getPre();
            if(last == null){
                first = null;
            } else {
                last.setNext(null);
            }
        }
    }

    private Entry<K,V> getEntry(K key) {
        return data.get(key);
    }

}
