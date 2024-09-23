package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import com.github.chandrakanthrck.cache_project.repository.CacheRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class PersistentCacheService {

    private static final Logger logger = Logger.getLogger(PersistentCacheService.class.getName());
    private final CacheRepository cacheRepository;
    private final MeterRegistry meterRegistry;

    public PersistentCacheService(CacheRepository cacheRepository, MeterRegistry meterRegistry) {
        this.cacheRepository = cacheRepository;
        this.meterRegistry = meterRegistry;
        registerPersistentCacheMetrics();
    }

    // Store cache entry using cacheKey and cacheValue
    public void put(String cacheKey, String cacheValue) {
        CacheEntry entry = new CacheEntry(cacheKey, cacheValue);
        cacheRepository.save(entry);
        logger.info("Stored entry in persistent cache with key: " + cacheKey);
        meterRegistry.counter("cache.put", Tags.of("cache", "persistent")).increment();
    }

    // Retrieve cache value by cacheKey
    public String get(String cacheKey) {
        Optional<CacheEntry> cacheEntry = cacheRepository.findById(cacheKey);
        if (cacheEntry.isPresent()) {
            logger.info("Cache hit for persistent cache with key: " + cacheKey);
            meterRegistry.counter("cache.hit", Tags.of("cache", "persistent")).increment();
            return cacheEntry.get().getCacheValue();
        } else {
            logger.info("Cache miss for persistent cache with key: " + cacheKey);
            meterRegistry.counter("cache.miss", Tags.of("cache", "persistent")).increment();
            return null;
        }
    }

    // Remove cache entry by cacheKey
    public void remove(String cacheKey) {
        cacheRepository.deleteById(cacheKey);
        logger.info("Removed entry from persistent cache with key: " + cacheKey);
        meterRegistry.counter("cache.remove", Tags.of("cache", "persistent")).increment();
    }

    // Clear all cache entries
    public void clear() {
        cacheRepository.deleteAll();
        logger.info("Cleared all entries from persistent cache");
        meterRegistry.counter("cache.clear", Tags.of("cache", "persistent")).increment();
    }

    // Get the number of cache entries
    public int size() {
        int count = (int) cacheRepository.count();
        logger.info("Persistent cache size: " + count);
        meterRegistry.gauge("cache.size", Tags.of("cache", "persistent"), count);
        return count;
    }

    private void registerPersistentCacheMetrics() {
        // Register additional metrics for the persistent cache
        meterRegistry.gauge("cache.size", Tags.of("cache", "persistent"), this, PersistentCacheService::size);
    }
}
