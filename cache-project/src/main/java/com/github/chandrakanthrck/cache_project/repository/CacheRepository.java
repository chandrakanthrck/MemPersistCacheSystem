package com.github.chandrakanthrck.cache_project.repository;

import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import org.springframework.data.repository.CrudRepository;

public interface CacheRepository extends CrudRepository<CacheEntry, String> {
    // The key is now 'cacheKey', so ensure it's correctly mapped
}
