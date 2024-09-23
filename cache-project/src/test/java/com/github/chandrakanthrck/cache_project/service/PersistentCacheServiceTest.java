package com.github.chandrakanthrck.cache_project.service;

import com.github.chandrakanthrck.cache_project.model.CacheEntry;
import com.github.chandrakanthrck.cache_project.repository.CacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersistentCacheServiceTest {

    @Mock
    private CacheRepository cacheRepository;

    @InjectMocks
    private PersistentCacheService persistentCacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPut() {
        String key = "key1";
        String value = "value1";

        // Call the method being tested
        persistentCacheService.put(key, value);

        // Use ArgumentCaptor to capture the CacheEntry argument
        ArgumentCaptor<CacheEntry> captor = ArgumentCaptor.forClass(CacheEntry.class);
        verify(cacheRepository).save(captor.capture());

        CacheEntry capturedEntry = captor.getValue();

        // Assert that the key and value inside CacheEntry are correct
        assertEquals(key, capturedEntry.getCacheKey(), "The key inside CacheEntry is incorrect");
        assertEquals(value, capturedEntry.getCacheValue(), "The value inside CacheEntry is incorrect");
    }
}
