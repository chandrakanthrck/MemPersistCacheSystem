package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.metrics.CacheMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SynchronizedCacheService {

    private static final Logger logger = Logger.getLogger(SynchronizedCacheService.class.getName());

    private final InMemoryCacheService<String, String> inMemoryCacheService;
    private final PersistentCacheService persistentCacheService;
    private final CacheMetrics cacheMetrics;
    private final MeterRegistry meterRegistry;

    public SynchronizedCacheService(InMemoryCacheService<String, String> inMemoryCacheService,
                                    PersistentCacheService persistentCacheService,
                                    CacheMetrics cacheMetrics,
                                    MeterRegistry meterRegistry) {
        this.inMemoryCacheService = inMemoryCacheService;
        this.persistentCacheService = persistentCacheService;
        this.cacheMetrics = cacheMetrics;
        this.meterRegistry = meterRegistry;
        registerMetrics();  // Register cache synchronization-related metrics
    }

    public void put(String key, String value) {
        logger.info("Putting key: " + key + " into in-memory cache.");
        inMemoryCacheService.put(key, value);
        syncToPersistentStore(key, value);  // Async sync to persistent store
        cacheMetrics.recordHit();
        meterRegistry.counter("cache.put", Tags.of("cache", "synchronized")).increment();
    }

    public String get(String key) {
        logger.info("Getting key: " + key + " from in-memory cache.");
        String value = inMemoryCacheService.get(key);
        if (value == null) {
            logger.info("Cache miss for key: " + key + ". Fetching from persistent store.");
            value = persistentCacheService.get(key);
            if (value != null) {
                logger.info("Putting key: " + key + " into in-memory cache after fetch from persistent store.");
                inMemoryCacheService.put(key, value);
            }
            cacheMetrics.recordMiss();
            meterRegistry.counter("cache.miss", Tags.of("cache", "synchronized")).increment();
        } else {
            logger.info("Cache hit for key: " + key);
            cacheMetrics.recordHit();
        }
        return value;
    }

    public void remove(String key) {
        logger.info("Removing key: " + key + " from both in-memory and persistent caches.");
        inMemoryCacheService.remove(key);
        persistentCacheService.remove(key);
        meterRegistry.counter("cache.remove", Tags.of("cache", "synchronized")).increment();
    }

    public void clear() {
        logger.info("Clearing all entries from both in-memory and persistent caches.");
        inMemoryCacheService.clear();
        persistentCacheService.clear();
        meterRegistry.counter("cache.clear", Tags.of("cache", "synchronized")).increment();
    }

    @Async
    public void syncToPersistentStore(String key, String value) {
        logger.info("Synchronizing key: " + key + " to persistent store.");
        persistentCacheService.put(key, value);
        meterRegistry.counter("cache.sync", Tags.of("cache", "synchronized")).increment();
    }

    public CacheMetrics getMetrics() {
        return cacheMetrics;
    }

    // Get the total size of both in-memory and persistent caches
    public int size() {
        int totalSize = inMemoryCacheService.size() + persistentCacheService.size();
        logger.info("Total cache size: " + totalSize);
        meterRegistry.gauge("cache.size.total", Tags.of("cache", "synchronized"), totalSize);
        return totalSize;
    }

    // Get the size of the in-memory cache
    public int getInMemoryCacheSize() {
        int inMemorySize = inMemoryCacheService.size();
        logger.info("In-memory cache size: " + inMemorySize);
        meterRegistry.gauge("cache.size.memory", Tags.of("cache", "synchronized"), inMemorySize);
        return inMemorySize;
    }

    // Get the size of the persistent cache
    public int getPersistentCacheSize() {
        int persistentSize = persistentCacheService.size();
        logger.info("Persistent cache size: " + persistentSize);
        meterRegistry.gauge("cache.size.persistent", Tags.of("cache", "synchronized"), persistentSize);
        return persistentSize;
    }

    private void registerMetrics() {
        // Register initial metrics for cache synchronization
        meterRegistry.gauge("cache.size.memory", Tags.of("cache", "synchronized"), this, SynchronizedCacheService::getInMemoryCacheSize);
        meterRegistry.gauge("cache.size.persistent", Tags.of("cache", "synchronized"), this, SynchronizedCacheService::getPersistentCacheSize);
        meterRegistry.gauge("cache.size.total", Tags.of("cache", "synchronized"), this, SynchronizedCacheService::size);
    }
}
