package com.github.chandrakanthrck.cache_project.repository;

import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends JpaRepository<CacheEntry, String> {
}
