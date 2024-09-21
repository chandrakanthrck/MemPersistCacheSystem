package com.github.chandrakanthrck.cache_project.eviction;

import java.util.concurrent.atomic.AtomicInteger;

public class CacheValue<V> {
    private final V value;
    private final long createdAt;
    private final AtomicInteger accessCount;

    public CacheValue(V value) {
        this.value = value;
        this.createdAt = System.currentTimeMillis();
        this.accessCount = new AtomicInteger(1);
    }

    public V getValue() {
        return value;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getAccessCount() {
        return accessCount.get();
    }

    public void incrementAccessCount() {
        this.accessCount.incrementAndGet();
    }

    public boolean isExpired(long ttl) {
        return System.currentTimeMillis() - createdAt > ttl;
    }
}
