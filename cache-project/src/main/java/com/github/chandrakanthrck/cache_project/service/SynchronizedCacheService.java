package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.metrics.CacheMetrics;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SynchronizedCacheService {

    private final InMemoryCacheService<String, String> inMemoryCacheService;
    private final PersistentCacheService persistentCacheService;
    private final CacheMetrics cacheMetrics;

    public SynchronizedCacheService(InMemoryCacheService<String, String> inMemoryCacheService,
                                    PersistentCacheService persistentCacheService,
                                    CacheMetrics cacheMetrics) {
        this.inMemoryCacheService = inMemoryCacheService;
        this.persistentCacheService = persistentCacheService;
        this.cacheMetrics = cacheMetrics;
    }

    public void put(String key, String value) {
        inMemoryCacheService.put(key, value);
        syncToPersistentStore(key, value);
        cacheMetrics.recordHit();
    }

    public String get(String key) {
        String value = inMemoryCacheService.get(key);
        if (value == null) {
            value = persistentCacheService.get(key);
            if (value != null) {
                inMemoryCacheService.put(key, value);
            }
            cacheMetrics.recordMiss();
        } else {
            cacheMetrics.recordHit();
        }
        return value;
    }

    public void remove(String key) {
        inMemoryCacheService.remove(key);
        persistentCacheService.remove(key);
    }

    public void clear() {
        inMemoryCacheService.clear();
        persistentCacheService.clear();
    }

    @Async
    public void syncToPersistentStore(String key, String value) {
        persistentCacheService.put(key, value);
    }

    public CacheMetrics getMetrics() {
        return cacheMetrics;
    }

    public int size() {
        return inMemoryCacheService.size() + persistentCacheService.size();
    }
}
