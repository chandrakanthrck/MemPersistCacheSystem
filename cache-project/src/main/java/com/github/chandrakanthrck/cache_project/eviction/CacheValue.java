package com.github.chandrakanthrck.cache_project.eviction;

public class CacheValue<V> {
    private final V value;
    private final long createdAt;
    private int accessCount;

    public CacheValue(V value) {
        this.value = value;
        this.createdAt = System.currentTimeMillis();
        this.accessCount = 1;  // Starts with 1 since it's accessed on put
    }

    public V getValue() {
        return value;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void incrementAccessCount() {
        this.accessCount++;
    }
}
