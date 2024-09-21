package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.eviction.CacheValue;
import com.github.chandrakanthrck.cache_project.metrics.CacheMetrics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

public class SynchronizedCacheService<K> {
    private final InMemoryCacheService<K, String> inMemoryCacheService; // Store String directly
    private final PersistentCacheService<K> persistentCacheService;
    private final CacheMetrics cacheMetrics;

    public SynchronizedCacheService(InMemoryCacheService<K, String> inMemoryCacheService,
                                    PersistentCacheService<K> persistentCacheService,
                                    CacheMetrics cacheMetrics) {
        this.inMemoryCacheService = inMemoryCacheService;
        this.persistentCacheService = persistentCacheService;
        this.cacheMetrics = cacheMetrics;
    }

    public void put(K key, CacheValue<String> value) {
        inMemoryCacheService.put(key, value.getValue()); // Use getValue() for putting
        syncToPersistentStore(key, value);
        cacheMetrics.recordHit();
    }

    public CacheValue<String> get(K key) {
        String value = inMemoryCacheService.get(key); // Directly get String
        if (value == null) {
            value = persistentCacheService.get(key);
            if (value != null) {
                inMemoryCacheService.put(key, new CacheValue<>(value)); // Wrap in CacheValue
            }
            cacheMetrics.recordMiss();
        } else {
            cacheMetrics.recordHit();
        }
        return new CacheValue<>(value); // Wrap in CacheValue before returning
    }

    // other methods remain unchanged
}
