package com.github.chandrakanthrck.cache_project.eviction;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class LFUEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    private final int maxSize;

    public LFUEvictionPolicy(int maxSize) {
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
            K lfuKey = null;
            int minAccessCount = Integer.MAX_VALUE;

            for (Map.Entry<K, V> entry : cache.entrySet()) {
                int accessCount = ((CacheValue<V>) entry.getValue()).getAccessCount();  // Cast to CacheValue
                if (accessCount < minAccessCount) {
                    minAccessCount = accessCount;
                    lfuKey = entry.getKey();
                }
            }

            if (lfuKey != null) {
                cache.remove(lfuKey);
            }
        }
    }
}
