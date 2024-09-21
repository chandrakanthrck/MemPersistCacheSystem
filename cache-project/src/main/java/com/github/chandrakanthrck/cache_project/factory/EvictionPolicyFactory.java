package com.github.chandrakanthrck.cache_project.factory;

import com.github.chandrakanthrck.cache_project.eviction.*;

import java.util.concurrent.TimeUnit;

public class EvictionPolicyFactory<K, V> { // Keep both K and V
    public enum EvictionType {
        TTL, LRU, LFU
    }

    public EvictionPolicy<K, CacheValue<V>> getEvictionPolicy(EvictionType evictionType) {
        switch (evictionType) {
            case TTL:
                return new TTLEvictionPolicy<>(10, TimeUnit.SECONDS);
            case LRU:
                return new LRUEvictionPolicy<>(100);
            case LFU:
                return new LFUEvictionPolicy<>(100);
            default:
                throw new IllegalArgumentException("Unknown eviction type: " + evictionType);
        }
    }
}
