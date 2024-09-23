package com.github.chandrakanthrck.cache_project.factory;

import com.github.chandrakanthrck.cache_project.eviction.*;

import java.util.concurrent.TimeUnit;

public class EvictionPolicyFactory<K> {

    public enum EvictionType {
        TTL, LRU, LFU
    }

    public EvictionPolicy<K, String> getEvictionPolicy(EvictionType evictionType) {
        switch (evictionType) {
            case TTL:
                return new TTLEvictionPolicy<>(10, TimeUnit.SECONDS);  // Directly using String
            case LRU:
                return new LRUEvictionPolicy<>(100);  // LRU eviction with String
            case LFU:
                return new LFUEvictionPolicy<>(100);  // LFU eviction with String
            default:
                throw new IllegalArgumentException("Unknown eviction type: " + evictionType);
        }
    }
}
