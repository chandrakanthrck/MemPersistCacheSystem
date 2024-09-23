package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.eviction.EvictionPolicy;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class InMemoryCacheService<K, V> implements CacheService<K, V> {

    private static final Logger logger = Logger.getLogger(InMemoryCacheService.class.getName());

    private final ConcurrentMap<K, V> cache;
    private final EvictionPolicy<K, V> evictionPolicy;
    private final MeterRegistry meterRegistry;
    private final int maxCacheSize;  // Define maxCacheSize

    public InMemoryCacheService(EvictionPolicy<K, V> evictionPolicy, MeterRegistry meterRegistry, int maxCacheSize) {
        this.cache = new ConcurrentHashMap<>();
        this.evictionPolicy = evictionPolicy;
        this.meterRegistry = meterRegistry;
        this.maxCacheSize = maxCacheSize;  // Set max cache size
        registerCacheMetrics();  // Register cache-related metrics
    }

    @Override
    public void put(K key, V value) {
        // Log cache size before eviction
        logger.info("Cache size before put: " + cache.size());

        // Evict entries if the cache is at or above the max size
        if (cache.size() >= maxCacheSize) {
            logger.info("Cache is full, performing eviction...");
            evictionPolicy.evictEntries(cache);
        }

        // Add key-value pair to the cache
        cache.put(key, value);

        // Log cache size after insertion
        logger.info("Cache size after put: " + cache.size());

        // Update metrics
        meterRegistry.gauge("cache.size", Tags.of("cache", "inMemoryCache"), cache.size());
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);

        // Track cache hits and misses
        if (value != null) {
            meterRegistry.counter("cache.hit", Tags.of("cache", "inMemoryCache")).increment();
            evictionPolicy.onGet(key, value);  // Notify eviction policy about access
        } else {
            meterRegistry.counter("cache.miss", Tags.of("cache", "inMemoryCache")).increment();
        }

        return value;
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        meterRegistry.gauge("cache.size", Tags.of("cache", "inMemoryCache"), cache.size());
    }

    @Override
    public void clear() {
        cache.clear();
        meterRegistry.gauge("cache.size", Tags.of("cache", "inMemoryCache"), 0);  // Reset cache size metric
    }

    @Override
    public int size() {
        return cache.size();
    }

    // Optionally, you can trigger manual eviction if needed
    public void evictEntries() {
        evictionPolicy.evictEntries(cache);
    }

    private void registerCacheMetrics() {
        // Register initial metrics for cache
        meterRegistry.gauge("cache.size", Tags.of("cache", "inMemoryCache"), cache, ConcurrentMap::size);
    }
}
