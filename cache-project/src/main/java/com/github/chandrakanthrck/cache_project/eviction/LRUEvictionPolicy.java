package com.github.chandrakanthrck.cache_project.eviction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class LRUEvictionPolicy<K, V> implements EvictionPolicy<K, V> {

    private static final Logger logger = Logger.getLogger(LRUEvictionPolicy.class.getName());
    private final LinkedHashMap<K, V> lruCache;
    private final int maxSize;

    public LRUEvictionPolicy(int maxSize) {
        this.maxSize = maxSize;
        this.lruCache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    @Override
    public void onPut(K key, V value, ConcurrentMap<K, V> cache) {
        lruCache.put(key, value);  // Add to LRU cache
        logger.info("LRU onPut: Added " + key + " to LRU tracking.");
        evictEntries(cache);  // Check if eviction is needed
    }

    @Override
    public void onGet(K key, V value) {
        lruCache.get(key);  // Access the item, reordering it in the LRU cache
        logger.info("LRU onGet: Accessed " + key);
    }

    @Override
    public void evictEntries(ConcurrentMap<K, V> cache) {
        if (lruCache.size() > maxSize) {
            K eldestKey = lruCache.keySet().iterator().next();  // Get eldest (LRU) key
            cache.remove(eldestKey);  // Remove the eldest entry from the actual cache
            lruCache.remove(eldestKey);  // Remove from LRU tracking
            logger.info("Evicted LRU: Removed " + eldestKey);
        }
    }
}
