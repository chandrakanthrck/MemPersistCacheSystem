package com.github.chandrakanthrck.cache_project.repository;

import com.github.chandrakanthrck.cache_project.eviction.CacheValue;
import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import org.springframework.data.repository.CrudRepository;

public interface CacheRepository<K> extends CrudRepository<CacheEntry<K, CacheValue<String>>, K> {
    // Custom query methods can be added here if necessary
}
