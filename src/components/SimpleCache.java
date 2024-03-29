package components;

import java.util.Map;
import java.util.HashMap;

public class SimpleCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;
    private final java.util.LinkedList<K> accessOrder;

    public SimpleCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.accessOrder = new java.util.LinkedList<>();
    }

    public synchronized V get(K key) {
        V value = cache.get(key);
        if (value != null) {
            accessOrder.remove(key);
            accessOrder.addFirst(key);
        }
        return value;
    }

    public synchronized void put(K key, V value) {
        if (cache.containsKey(key)) {
            accessOrder.remove(key);
        } else if (cache.size() >= capacity) {
            K leastRecentlyUsed = accessOrder.removeLast();
            cache.remove(leastRecentlyUsed);
        }
        cache.put(key, value);
        accessOrder.addFirst(key);
    }

    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public synchronized void remove(K key) {
        cache.remove(key);
        accessOrder.remove(key);
    }

    public synchronized int size() {
        return cache.size();
    }

    public synchronized void clear() {
        cache.clear();
        accessOrder.clear();
    }
}