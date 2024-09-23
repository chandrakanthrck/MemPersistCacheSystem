package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import com.github.chandrakanthrck.cache_project.repository.CacheRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersistentCacheService {

    private final CacheRepository cacheRepository;

    public PersistentCacheService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    // Store cache entry using cacheKey and cacheValue
    public void put(String cacheKey, String cacheValue) {
        CacheEntry entry = new CacheEntry(cacheKey, cacheValue);
        cacheRepository.save(entry);
    }

    // Retrieve cache value by cacheKey
    public String get(String cacheKey) {
        Optional<CacheEntry> cacheEntry = cacheRepository.findById(cacheKey);
        return cacheEntry.map(CacheEntry::getCacheValue).orElse(null);
    }

    // Remove cache entry by cacheKey
    public void remove(String cacheKey) {
        cacheRepository.deleteById(cacheKey);
    }

    // Clear all cache entries
    public void clear() {
        cacheRepository.deleteAll();
    }

    // Get the number of cache entries
    public int size() {
        return (int) cacheRepository.count();
    }
}
