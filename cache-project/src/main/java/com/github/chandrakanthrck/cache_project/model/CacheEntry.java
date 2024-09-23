package com.github.chandrakanthrck.cache_project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CacheEntry {

    @Id
    private String cacheKey;  // Renamed from 'key' to 'cacheKey' to avoid conflicts

    private String cacheValue;

    // Default constructor
    public CacheEntry() {}

    // Constructor with parameters
    public CacheEntry(String cacheKey, String cacheValue) {
        this.cacheKey = cacheKey;
        this.cacheValue = cacheValue;
    }

    // Getters and setters
    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheValue() {
        return cacheValue;
    }

    public void setCacheValue(String cacheValue) {
        this.cacheValue = cacheValue;
    }
}
