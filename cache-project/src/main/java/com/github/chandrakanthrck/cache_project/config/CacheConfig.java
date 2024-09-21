package com.github.chandrakanthrck.cache_project.config;

import com.github.chandrakanthrck.cache_project.factory.EvictionPolicyFactory;
import com.github.chandrakanthrck.cache_project.metrics.CacheMetrics;
import com.github.chandrakanthrck.cache_project.repository.CacheRepository;
import com.github.chandrakanthrck.cache_project.service.InMemoryCacheService;
import com.github.chandrakanthrck.cache_project.service.PersistentCacheService;
import com.github.chandrakanthrck.cache_project.service.SynchronizedCacheService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public InMemoryCacheService<String, String> inMemoryCacheService(
            EvictionPolicyFactory<String, String> evictionPolicyFactory,
            MeterRegistry meterRegistry) {
        return new InMemoryCacheService<>(evictionPolicyFactory.getEvictionPolicy(EvictionPolicyFactory.EvictionType.LRU), meterRegistry);
    }

    @Bean
    public PersistentCacheService<String> persistentCacheService(CacheRepository<String> cacheRepository) {
        return new PersistentCacheService<>(cacheRepository);
    }

    @Bean
    public SynchronizedCacheService<String> synchronizedCacheService(
            InMemoryCacheService<String, String> inMemoryCacheService,
            PersistentCacheService<String> persistentCacheService,
            CacheMetrics cacheMetrics) {
        return new SynchronizedCacheService<>(inMemoryCacheService, persistentCacheService, cacheMetrics);
    }
}
