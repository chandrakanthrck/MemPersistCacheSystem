package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.eviction.CacheValue;
import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import com.github.chandrakanthrck.cache_project.repository.CacheRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PersistentCacheService<K> implements CacheService<K, String> { // Use String directly
    private final CacheRepository<K> cacheRepository;

    public PersistentCacheService(CacheRepository<K> cacheRepository) {
        this.cacheRepository = cacheRepository;
    }


    @Override
    public void put(K key, String value) {
        CacheEntry<K, String> entry = new CacheEntry<>();
        entry.setKey(key);
        entry.setValue(value);
        cacheRepository.save(entry);
    }

    @Override
    public CacheValue<String> get(K key) {
        Optional<CacheEntry<K, CacheValue<String>>> cacheEntry = cacheRepository.findById(key);
        return cacheEntry.map(CacheEntry::getValue).orElse(null);
    }

    @Override
    public void remove(K key) {
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
