package com.github.chandrakanthrck.cache_project.eviction;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class LRUEvictionPolicy<K, V> implements EvictionPolicy<K, CacheValue<V>> {

    private final int maxSize;

    public LRUEvictionPolicy(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void onPut(K key, CacheValue<V> value, ConcurrentMap<K, CacheValue<V>> cache) {
        if (cache.size() >= maxSize) {
            evictEntries(cache);
        }
        cache.put(key, value);
    }

    @Override
    public void evictEntries(ConcurrentMap<K, CacheValue<V>> cache) {
        if (cache.size() > maxSize) {
            K lruKey = null;
            long oldestTimestamp = Long.MAX_VALUE;

            for (Map.Entry<K, CacheValue<V>> entry : cache.entrySet()) {
                if (entry.getValue().getCreatedAt() < oldestTimestamp) {
                    oldestTimestamp = entry.getValue().getCreatedAt();
                    lruKey = entry.getKey();
                }
            }

            if (lruKey != null) {
                cache.remove(lruKey);
            }
        }
    }
}
