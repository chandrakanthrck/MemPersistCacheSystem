package com.github.chandrakanthrck.cache_project.eviction;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class TTLEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    private final long ttl;  // Time-to-live in milliseconds

    public TTLEvictionPolicy(long ttl, TimeUnit timeUnit) {
        this.ttl = timeUnit.toMillis(ttl);
    }

    @Override
    public void onPut(K key, V value, ConcurrentMap<K, V> cache) {
        cache.put(key, value);  // Add to cache
    }

    @Override
    public void evictEntries(ConcurrentMap<K, V> cache) {
        long now = System.currentTimeMillis();

        // Evict entries that have exceeded their TTL
        for (Map.Entry<K, V> entry : cache.entrySet()) {
            if (now > ((CacheValue<V>) entry.getValue()).getCreatedAt() + ttl) {  // Cast to CacheValue to access the timestamp
                cache.remove(entry.getKey());
            }
        }
    }
}
