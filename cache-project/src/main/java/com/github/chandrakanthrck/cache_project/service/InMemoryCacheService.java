package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.eviction.EvictionPolicy;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryCacheService<K, V> implements CacheService<K, V> {
    private final ConcurrentMap<K, V> cache;
    private final EvictionPolicy<K, V> evictionPolicy;

    public InMemoryCacheService(EvictionPolicy<K, V> evictionPolicy, MeterRegistry meterRegistry) {
        this.cache = new ConcurrentHashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public void put(K key, V value) {
        evictionPolicy.onPut(key, value, cache);  // Directly store V
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);  // Directly retrieve V
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    public void evictEntries() {
        evictionPolicy.evictEntries(cache);
    }
}
