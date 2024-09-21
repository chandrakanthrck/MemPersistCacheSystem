package com.github.chandrakanthrck.cache_project.metrics;

import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Component;

@Component
public class CacheMetrics {

    private final Counter hitCounter;
    private final Counter missCounter;
    private final Counter evictionCounter;

    public CacheMetrics() {
        // Registering custom metrics with Micrometer
        this.hitCounter = meterRegistry.counter("cache.hit", "type", "cache");
        this.missCounter = meterRegistry.counter("cache.miss", "type", "cache");
        this.evictionCounter = meterRegistry.counter("cache.eviction", "type", "cache");
    }

    // Record a cache hit
    public void recordHit() {
        hitCounter.increment();  // Micrometer counter for cache hits
    }

    // Record a cache miss
    public void recordMiss() {
        missCounter.increment();  // Micrometer counter for cache misses
    }

    // Record a cache eviction
    public void recordEviction() {
        evictionCounter.increment();  // Micrometer counter for cache evictions
    }

    // Optional methods for debugging or other purposes
    public double getHitCount() {
        return hitCounter.count();  // Returns the current hit count
    }

    public double getMissCount() {
        return missCounter.count();  // Returns the current miss count
    }

    public double getEvictionCount() {
        return evictionCounter.count();  // Returns the current eviction count
    }

    // Hit and miss rates calculation based on Micrometer counts
    public double getHitRate() {
        double totalRequests = hitCounter.count() + missCounter.count();
        return totalRequests == 0 ? 0 : hitCounter.count() / totalRequests;
    }

    public double getMissRate() {
        double totalRequests = hitCounter.count() + missCounter.count();
        return totalRequests == 0 ? 0 : missCounter.count() / totalRequests;
    }
}
