package com.github.chandrakanthrck.cache_project.eviction;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class TTLEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    private static final Logger logger = Logger.getLogger(TTLEvictionPolicy.class.getName());
    private final ConcurrentMap<K, Instant> timeMap;  // Use a thread-safe map for time tracking
    private final long ttlMillis;
    private final boolean refreshOnGet;  // Option to refresh the TTL on access

    public TTLEvictionPolicy(long ttlMillis, boolean refreshOnGet) {
        this.timeMap = new ConcurrentHashMap<>();
        this.ttlMillis = ttlMillis;
        this.refreshOnGet = refreshOnGet;
    }

    @Override
    public void onPut(K key, V value, ConcurrentMap<K, V> cache) {
        timeMap.put(key, Instant.now());  // Store the current timestamp when the entry is added
        evictEntries(cache);  // Check if eviction is needed
    }

    @Override
    public void onGet(K key, V value) {
        if (refreshOnGet) {
            timeMap.put(key, Instant.now());  // Optionally refresh the timestamp on access
        }
    }

    @Override
    public void evictEntries(ConcurrentMap<K, V> cache) {
        Instant now = Instant.now();

        // Iterate through the time map and remove entries older than TTL
        timeMap.entrySet().removeIf(entry -> {
            K key = entry.getKey();
            Instant timeAdded = entry.getValue();
            if (now.isAfter(timeAdded.plusMillis(ttlMillis))) {
                cache.remove(key);  // Remove expired entry from cache
                logger.info("Evicted TTL: Removed " + key);
                return true;  // Remove from the time map
            }
            return false;
        });
    }
}
