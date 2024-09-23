package com.github.chandrakanthrck.cache_project.factory;

import com.github.chandrakanthrck.cache_project.eviction.*;

import java.util.concurrent.TimeUnit;

public class EvictionPolicyFactory<K, V> {

    public enum EvictionType {
        TTL, LRU, LFU
    }

    /**
     * Returns an eviction policy based on the provided eviction type, cache size, and TTL parameters.
     *
     * @param evictionType  the type of eviction policy (TTL, LRU, LFU)
     * @param cacheSize     the maximum size of the cache (only relevant for LRU and LFU)
     * @param ttlMillis     the TTL in milliseconds (only relevant for TTL eviction)
     * @param refreshOnGet  whether the TTL should be refreshed on access (only relevant for TTL eviction)
     * @return an EvictionPolicy based on the input parameters
     */
    public EvictionPolicy<K, V> getEvictionPolicy(EvictionType evictionType, int cacheSize, long ttlMillis, boolean refreshOnGet) {
        switch (evictionType) {
            case TTL:
                validateTtl(ttlMillis);
                return new TTLEvictionPolicy<>(ttlMillis, refreshOnGet);  // TTL eviction with configurable duration and refresh behavior
            case LRU:
                validateCacheSize(cacheSize);
                return new LRUEvictionPolicy<>(cacheSize);  // LRU eviction with configurable cache size
            case LFU:
                validateCacheSize(cacheSize);
                return new LFUEvictionPolicy<>();  // LFU eviction
            default:
                throw new IllegalArgumentException("Unknown eviction type: " + evictionType);
        }
    }

    /**
     * Returns an eviction policy with default values.
     *
     * @param evictionType the type of eviction policy (TTL, LRU, LFU)
     * @return an EvictionPolicy based on the default parameters
     */
    public EvictionPolicy<K, V> getEvictionPolicy(EvictionType evictionType) {
        return getEvictionPolicy(evictionType, 100, TimeUnit.SECONDS.toMillis(10), false);  // Default values
    }

    // Helper method to validate cache size
    private void validateCacheSize(int cacheSize) {
        if (cacheSize <= 0) {
            throw new IllegalArgumentException("Cache size must be greater than 0");
        }
    }

    // Helper method to validate TTL duration
    private void validateTtl(long ttlMillis) {
        if (ttlMillis <= 0) {
            throw new IllegalArgumentException("TTL must be greater than 0 milliseconds");
        }
    }
}
