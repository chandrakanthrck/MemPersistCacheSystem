package com.github.chandrakanthrck.cache_project.eviction;

import java.util.concurrent.ConcurrentMap;

public interface EvictionPolicy<K, V> {

    // Method to handle eviction after a put operation
    void onPut(K key, V value, ConcurrentMap<K, V> cache);

    // Method to evict expired or least used entries periodically
    void evictEntries(ConcurrentMap<K, V> cache);
}
