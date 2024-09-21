//package com.github.chandrakanthrck.cache_project.service;
//
//import com.github.chandrakanthrck.cache_project.eviction.CacheValue;
//import com.github.chandrakanthrck.cache_project.eviction.EvictionPolicy;
//import com.github.chandrakanthrck.cache_project.factory.EvictionPolicyFactory;
//import com.github.chandrakanthrck.cache_project.metrics.CacheMetrics;
//import io.micrometer.core.instrument.MeterRegistry;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.*;
//
//class SynchronizedCacheServiceTest {
//
//    @Mock
//    private CacheService<String, Object> persistentCacheServiceMock;
//
//    @Mock
//    private CacheMetrics cacheMetricsMock;
//
//    @Mock
//    private MeterRegistry meterRegistry;
//
//    private SynchronizedCacheService<String, Object> cacheService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Mock the MeterRegistry counters for hits, misses, and evictions
//        when(meterRegistry.counter("cache.hit", "type", "in_memory")).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
//        when(meterRegistry.counter("cache.miss", "type", "in_memory")).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
//        when(meterRegistry.counter("cache.eviction", "type", "in_memory")).thenReturn(mock(io.micrometer.core.instrument.Counter.class));
//
//        // Manually instantiate SynchronizedCacheService with mocks
//        cacheService = new SynchronizedCacheService<>(cacheMetricsMock, persistentCacheServiceMock, EvictionPolicyFactory.EvictionType.LRU, meterRegistry);
//    }
//
//    @Test
//    void testPutAndGet() {
//        // Act: Put and then get a cache entry
//        cacheService.put("key1", "value1");
//        cacheService.get("key1");
//
//        // Verify that a hit is recorded when the item is retrieved
//        verify(cacheMetricsMock, times(1)).recordHit();
//    }
//}
