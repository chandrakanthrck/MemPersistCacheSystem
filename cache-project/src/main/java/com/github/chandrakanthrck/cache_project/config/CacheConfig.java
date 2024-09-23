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

    // Define the EvictionPolicyFactory bean
    @Bean
    public EvictionPolicyFactory<String> evictionPolicyFactory() {
        return new EvictionPolicyFactory<>();  // Instantiate the factory
    }

    @Bean
    public InMemoryCacheService<String, String> inMemoryCacheService(
            EvictionPolicyFactory<String> evictionPolicyFactory,
            MeterRegistry meterRegistry) {
        return new InMemoryCacheService<>(
                evictionPolicyFactory.getEvictionPolicy(EvictionPolicyFactory.EvictionType.LRU),
                meterRegistry);
    }

    @Bean
    public PersistentCacheService persistentCacheService(CacheRepository cacheRepository) {
        return new PersistentCacheService(cacheRepository);
    }

    @Bean
    public SynchronizedCacheService synchronizedCacheService(
            InMemoryCacheService<String, String> inMemoryCacheService,
            PersistentCacheService persistentCacheService,
            CacheMetrics cacheMetrics) {
        return new SynchronizedCacheService(inMemoryCacheService, persistentCacheService, cacheMetrics);
    }
}
