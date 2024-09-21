package com.github.chandrakanthrck.cache_project.eviction;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class TTLEvictionPolicy<K, V> implements EvictionPolicy<K, CacheValue<V>> {

    private final long ttl;

    public TTLEvictionPolicy(long ttl, TimeUnit timeUnit) {
        this.ttl = timeUnit.toMillis(ttl);
    }

    @Override
    public void onPut(K key, CacheValue<V> value, ConcurrentMap<K, CacheValue<V>> cache) {
        cache.put(key, value);  // Add to cache
    }

    @Override
    public void evictEntries(ConcurrentMap<K, CacheValue<V>> cache) {
        long now = System.currentTimeMillis();

        // Evict entries that have exceeded their TTL
        for (Map.Entry<K, CacheValue<V>> entry : cache.entrySet()) {
            if (now > entry.getValue().getCreatedAt() + ttl) {
                cache.remove(entry.getKey());
            }
        }
    }
}
