package com.github.chandrakanthrck.cache_project;

import com.github.chandrakanthrck.cache_project.service.SynchronizedCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(classes = {CacheProjectApplication.class})
class CacheProjectApplicationTests {

    @Autowired
    private SynchronizedCacheService cacheService;

    @BeforeEach
    void setUp() {
        assertNotNull(cacheService, "CacheService should be loaded by the ApplicationContext.");
    }

    @Test
    @DirtiesContext
    void testCacheAndPersistentStore() {
        cacheService.put("key1", "value1");
        assertEquals("value1", cacheService.get("key1"));
        assertNotNull(cacheService.get("key1"));
    }

    @Test
    void testCacheMissAndUpdateFromPersistentStore() {
        cacheService.put("key2", "value2");
        cacheService.clear();
        assertNull(cacheService.get("key2"));
        cacheService.put("key2", "value2");
        assertEquals("value2", cacheService.get("key2"));
    }

    @Test
    void testClearCache() {
        cacheService.put("keyToClear", "valueToClear");
        assertEquals("valueToClear", cacheService.get("keyToClear"));
        cacheService.clear();
        assertNull(cacheService.get("keyToClear"));
    }

    @Test
    void testCacheSize() {
        cacheService.put("keySize1", "valueSize1");
        cacheService.put("keySize2", "valueSize2");
        assertTrue(cacheService.size() >= 2, "Cache size should be at least 2.");
    }

    @Test
    void contextLoads() {
        // Basic context loading test
        assertNotNull(cacheService, "The application context should load the CacheService bean.");
    }
}
