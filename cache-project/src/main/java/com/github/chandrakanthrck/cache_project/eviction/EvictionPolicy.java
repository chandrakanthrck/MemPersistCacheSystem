package com.github.chandrakanthrck.cache_project.eviction;

import java.util.concurrent.ConcurrentMap;

public interface EvictionPolicy<K, V> {

    /**
     * This method is called when a new item is added to the cache.
     * The eviction logic (if any) should be applied here.
     *
     * @param key the key of the item being added
     * @param value the value of the item being added
     * @param cache the current state of the cache, represented as a ConcurrentMap
     */
    void onPut(K key, V value, ConcurrentMap<K, V> cache);

    /**
     * This method is responsible for evicting entries from the cache.
     * Different eviction policies can implement their custom logic here,
     * such as TTL expiration, LRU (least recently used), or LFU (least frequently used).
     *
     * @param cache the current state of the cache from which to evict entries
     */
    void evictEntries(ConcurrentMap<K, V> cache);
}
