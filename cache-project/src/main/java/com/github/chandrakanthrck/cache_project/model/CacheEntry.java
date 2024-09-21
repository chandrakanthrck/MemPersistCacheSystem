package com.github.chandrakanthrck.cache_project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cache_entries")
public class CacheEntry<K, V> {

    @Id
    private K key;  // Key type
    private V value;  // Value type

    // Getters and Setters
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
