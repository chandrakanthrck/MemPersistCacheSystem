package com.github.chandrakanthrck.cache_project.controller;

import com.github.chandrakanthrck.cache_project.metrics.CacheMetrics;
import com.github.chandrakanthrck.cache_project.service.SynchronizedCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache/metrics")
public class CacheMetricsController {

    private final SynchronizedCacheService<String, Object> cacheService;

    @Autowired
    public CacheMetricsController(SynchronizedCacheService<String, Object> cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping
    public CacheMetrics getCacheMetrics() {
        return cacheService.getMetrics();
    }
}
