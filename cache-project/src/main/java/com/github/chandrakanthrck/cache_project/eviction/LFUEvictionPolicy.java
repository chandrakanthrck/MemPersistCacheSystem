package com.github.chandrakanthrck.cache_project.eviction;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class LFUEvictionPolicy<K, V> implements EvictionPolicy<K, CacheValue<V>> {

    private final int maxSize;

    public LFUEvictionPolicy(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void onPut(K key, CacheValue<V> value, ConcurrentMap<K, CacheValue<V>> cache) {
        if (cache.size() >= maxSize) {
            evictEntries(cache);  // Evict if cache is full
        }
        cache.put(key, value);  // Add to cache
    }

    @Override
    public void evictEntries(ConcurrentMap<K, CacheValue<V>> cache) {
        if (cache.size() > maxSize) {
            K lfuKey = null;
            int minAccessCount = Integer.MAX_VALUE;

            // Find the least frequently accessed entry
            for (Map.Entry<K, CacheValue<V>> entry : cache.entrySet()) {
                if (entry.getValue().getAccessCount() < minAccessCount) {
                    minAccessCount = entry.getValue().getAccessCount();
                    lfuKey = entry.getKey();
                }
            }

            // Remove the least frequently used entry
            if (lfuKey != null) {
                cache.remove(lfuKey);
            }
        }
    }
}
