package com.github.chandrakanthrck.cache_project.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SynchronizedCacheService<K, V> implements CacheService<K, V>{
    private final CacheService<K, V> inMemoryCacheService;
    private final CacheService<K, V> persistentCacheService;
    @Autowired
    public SynchronizedCacheService(@Qualifier("inMemoryCacheService") CacheService<K, V> inMemoryCacheService,
                                    @Qualifier("persistentCacheService") CacheService<K, V> persistentCacheService){
        this.inMemoryCacheService = inMemoryCacheService;
        this.persistentCacheService = persistentCacheService;
    }
    @Override
    public void put(K key, V value) {
        inMemoryCacheService.put(key, value);
        persistentCacheService.put(key, value);
    }

    @Override
    public V get(K key) {
        V value = inMemoryCacheService.get(key);
        if(value == null){
            value = persistentCacheService.get(key);
            if(value == null){
                inMemoryCacheService.put(key, value);
            }
        }
        return  value;
    }

    @Override
    public void remove(K key) {
        inMemoryCacheService.remove(key);
        persistentCacheService.remove(key);
    }

    @Override
    public void clear() {
        inMemoryCacheService.clear();
        persistentCacheService.clear();
    }

    @Override
    public int size() {
        return inMemoryCacheService.size();
    }
}
