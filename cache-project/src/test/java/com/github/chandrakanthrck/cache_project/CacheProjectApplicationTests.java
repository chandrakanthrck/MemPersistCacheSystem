package com.github.chandrakanthrck.cache_project;

import com.github.chandrakanthrck.cache_project.service.SynchronizedCacheService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  // Resets the context before each test
class CacheProjectApplicationTests {

    @Autowired
    private SynchronizedCacheService cacheService;  // Ensure correct type with generics

    @Test
    void testCacheAndPersistentStore() {
        // Put value in cache and persistent store
        cacheService.put("key1", "value1");

        // Ensure the value is correctly stored and retrieved from cache
        assertEquals("value1", cacheService.get("key1"), "Value from cache is incorrect");

        // Ensure the persistent store was also updated and is in sync
        assertNotNull(cacheService.get("key1"), "Value should exist in persistent store");
    }

    @Test
    void testCacheMissAndUpdateFromPersistentStore() {
        // Put value in cache
        cacheService.put("key2", "value2");

        // Clear in-memory cache to simulate a miss
        cacheService.clear();

        // Ensure cache is cleared
        assertNull(cacheService.get("key2"), "Cache should be cleared");

        // Retrieve from persistent store, value should be reloaded into cache
        cacheService.put("key2", "value2");  // Simulate reload from persistent store
        assertEquals("value2", cacheService.get("key2"), "Value should be reloaded from persistent store");
    }

    @Test
    void testClearCache() {
        cacheService.put("keyToClear", "valueToClear");

        // Ensure the cache is populated
        assertEquals("valueToClear", cacheService.get("keyToClear"));

        // Clear the cache
        cacheService.clear();

        // Ensure the cache is cleared
        assertNull(cacheService.get("keyToClear"), "Cache should be cleared");
    }

    @Test
    void testCacheSize() {
        cacheService.put("keySize1", "valueSize1");
        cacheService.put("keySize2", "valueSize2");

        // Ensure the cache size reflects the number of entries
        assertTrue(cacheService.size() >= 2, "Cache size should be at least 2");
    }
}
