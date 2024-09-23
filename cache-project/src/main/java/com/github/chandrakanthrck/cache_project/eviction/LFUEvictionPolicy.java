package com.github.chandrakanthrck.cache_project.eviction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class LFUEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    private static final Logger logger = Logger.getLogger(LFUEvictionPolicy.class.getName());
    private final ConcurrentMap<K, Integer> accessCounts;

    public LFUEvictionPolicy() {
        this.accessCounts = new ConcurrentHashMap<>();
    }

    @Override
    public void onPut(K key, V value, ConcurrentMap<K, V> cache) {
        accessCounts.put(key, 1);  // Initialize the access count to 1 when a new entry is added
        evictEntries(cache);  // Check if eviction is needed
    }

    @Override
    public void onGet(K key, V value) {
        accessCounts.compute(key, (k, count) -> (count == null) ? 1 : count + 1);  // Increment access count
    }

    @Override
    public void evictEntries(ConcurrentMap<K, V> cache) {
        K leastFrequentKey = accessCounts.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (leastFrequentKey != null) {
            cache.remove(leastFrequentKey);  // Remove from actual cache
            accessCounts.remove(leastFrequentKey);  // Remove from access count tracking
            logger.info("Evicted LFU: Removed " + leastFrequentKey);
        }
    }
}
