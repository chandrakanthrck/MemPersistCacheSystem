package com.github.chandrakanthrck.cache_project.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryCacheService<K,V> implements CacheService<K, V>{
    private final ConcurrentMap<K, V> cache;

    public InMemoryCacheService() {
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }
}