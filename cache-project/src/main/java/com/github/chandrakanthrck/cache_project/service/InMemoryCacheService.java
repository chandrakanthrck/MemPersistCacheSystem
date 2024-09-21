package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.eviction.CacheValue;
import com.github.chandrakanthrck.cache_project.eviction.EvictionPolicy;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryCacheService<K> implements CacheService<K, String> { // Use String directly
    private final ConcurrentMap<K, String> cache;
    private final EvictionPolicy<K, String> evictionPolicy;

    public InMemoryCacheService(EvictionPolicy<K, String> evictionPolicy, MeterRegistry meterRegistry) {
        this.cache = new ConcurrentHashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public void put(K key, String value) {
        evictionPolicy.onPut(key, new CacheValue<>(value), cache); // Wrap in CacheValue
        cache.put(key, value);
    }

    @Override
    public String get(K key) {
        return cache.get(key);
    }

    @Override
    public void remove(K key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    // other methods remain unchanged
}
