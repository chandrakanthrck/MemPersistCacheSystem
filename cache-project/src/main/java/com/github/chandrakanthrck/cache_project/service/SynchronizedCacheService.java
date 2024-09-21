package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.eviction.CacheValue;
import com.github.chandrakanthrck.cache_project.eviction.EvictionPolicy;
import com.github.chandrakanthrck.cache_project.factory.EvictionPolicyFactory;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SynchronizedCacheService<K, V> implements CacheService<K, V> {
    private final ConcurrentMap<K, CacheValue<V>> inMemoryCache = new ConcurrentHashMap<>();
    private final CacheService<K, V> persistentCacheService;  // MySQL service
    private final EvictionPolicy<K, CacheValue<V>> evictionPolicy;

    // Micrometer counters for metrics
    private final Counter hitCounter;
    private final Counter missCounter;
    private final Counter evictionCounter;

    // Injecting MeterRegistry to create and manage Micrometer metrics
    @Autowired
    public SynchronizedCacheService(CacheService<K, V> persistentCacheService,
                                    EvictionPolicyFactory.EvictionType evictionType,
                                    MeterRegistry meterRegistry) {
        this.persistentCacheService = persistentCacheService;

        // Initialize the eviction policy using the factory
        EvictionPolicyFactory<K, V> factory = new EvictionPolicyFactory<>();
        this.evictionPolicy = factory.getEvictionPolicy(evictionType);

        // Initialize Micrometer counters
        this.hitCounter = meterRegistry.counter("cache.hit", "type", "in_memory");
        this.missCounter = meterRegistry.counter("cache.miss", "type", "in_memory");
        this.evictionCounter = meterRegistry.counter("cache.eviction", "type", "in_memory");
    }

    // Asynchronous method to write data to the persistent cache
    @Async("asyncExecutor")
    public void syncToPersistentStore(K key, V value) {
        persistentCacheService.put(key, value);  // Asynchronous write to MySQL
    }

    @Override
    public void put(K key, V value) {
        CacheValue<V> cacheValue = new CacheValue<>(value);
        evictionPolicy.onPut(key, cacheValue, inMemoryCache);

        // Persist to the MySQL persistent cache asynchronously
        syncToPersistentStore(key, value);
    }

    @Override
    public V get(K key) {
        // First, check the in-memory cache
        CacheValue<V> cacheValue = inMemoryCache.get(key);
        if (cacheValue == null) {
            // Record miss in Micrometer
            missCounter.increment();

            // If not found, check the persistent store (MySQL)
            V value = persistentCacheService.get(key);
            if (value != null) {
                put(key, value);  // Cache the value in memory
            }
            return value;
        }

        // Cache hit
        hitCounter.increment();
        cacheValue.incrementAccessCount();  // Increment access count for LFU
        return cacheValue.getValue();
    }

    @Override
    public void remove(K key) {
        // Remove from in-memory cache
        if (inMemoryCache.remove(key) != null) {
            evictionCounter.increment();  // Record eviction
        }
        persistentCacheService.remove(key);
    }

    @Override
    public void clear() {
        inMemoryCache.clear();
        persistentCacheService.clear();
    }

    @Override
    public int size() {
        return inMemoryCache.size();
    }

    // Method to manually evict entries (e.g., scheduled eviction)
    public void evict() {
        evictionPolicy.evictEntries(inMemoryCache);
        evictionCounter.increment();
    }
}