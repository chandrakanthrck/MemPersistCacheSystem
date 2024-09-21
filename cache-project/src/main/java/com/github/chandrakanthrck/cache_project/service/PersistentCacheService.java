package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import com.github.chandrakanthrck.cache_project.repository.CacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersistentCacheService implements CacheService<String, String> {
    private final CacheRepository cacheRepository;

    public PersistentCacheService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    @Override
    public void put(String key, String value) {
        CacheEntry entry = new CacheEntry();
        entry.setKey(key);
        entry.setValue(value);
        cacheRepository.save(entry);
    }

    @Override
    public String get(String key) {
        Optional<CacheEntry> cacheEntry = cacheRepository.findById(key);
        return cacheEntry.map(CacheEntry::getValue).orElse(null);
    }

    @Override
    public void remove(String key) {
        cacheRepository.deleteById(key);
    }

    @Override
    public void clear() {
        cacheRepository.deleteAll();
    }

    @Override
    public int size() {
        return (int) cacheRepository.count();
    }
}
