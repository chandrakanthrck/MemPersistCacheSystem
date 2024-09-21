package com.github.chandrakanthrck.cache_project.service;

public interface CacheService<K, V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key); // This method needs to be implemented
    void clear();
    int size();
}
