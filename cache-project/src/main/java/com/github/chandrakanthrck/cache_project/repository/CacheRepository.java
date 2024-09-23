package com.github.chandrakanthrck.cache_project.repository;

import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends CrudRepository<CacheEntry, String> {
    // Custom query methods (if needed) can be defined here
}
