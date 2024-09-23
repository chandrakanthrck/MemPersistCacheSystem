package com.github.chandrakanthrck.cache_project.eviction;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class LRUEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    private final int maxSize;

    public LRUEvictionPolicy(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void onPut(K key, V value, ConcurrentMap<K, V> cache) {
        if (cache.size() >= maxSize) {
            evictEntries(cache);  // Evict if cache is full
        }
        cache.put(key, value);  // Add to cache
    }

    @Override
    public void evictEntries(ConcurrentMap<K, V> cache) {
        if (cache.size() > maxSize) {
            K lruKey = null;
            long oldestTimestamp = Long.MAX_VALUE;

            for (Map.Entry<K, V> entry : cache.entrySet()) {
                long createdAt = ((CacheValue<V>) entry.getValue()).getCreatedAt();  // Cast to CacheValue
                if (createdAt < oldestTimestamp) {
                    oldestTimestamp = createdAt;
                    lruKey = entry.getKey();
                }
            }

            if (lruKey != null) {
                cache.remove(lruKey);
            }
        }
    }
}
