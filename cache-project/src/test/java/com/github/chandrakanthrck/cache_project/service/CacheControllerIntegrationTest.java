package com.github.chandrakanthrck.cache_project.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CacheControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SynchronizedCacheService synchronizedCacheService;

    @Test
    void testPutEntryToCache() throws Exception {
        // Simulate a POST request to put a cache entry
        mockMvc.perform(post("/cache/put")
                        .param("key", "testKey")
                        .param("value", "testValue")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string("Cached testKey"));

        // Verify that the service's put method was called with the expected parameters
        verify(synchronizedCacheService, times(1)).put("testKey", "testValue");
    }

    @Test
    void testGetEntryFromCache() throws Exception {
        // Mock the service to return a value when queried with a key
        when(synchronizedCacheService.get("testKey")).thenReturn("testValue");

        // Simulate a GET request to retrieve a cache entry
        mockMvc.perform(get("/cache/get")
                        .param("key", "testKey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("testValue"));

        // Verify that the service's get method was called with the expected key
        verify(synchronizedCacheService, times(1)).get("testKey");
    }

    @Test
    void testGetEntryNotFound() throws Exception {
        // Mock the service to return null for a non-existing key
        when(synchronizedCacheService.get("nonExistingKey")).thenReturn(null);

        // Simulate a GET request for a non-existing cache entry
        mockMvc.perform(get("/cache/get")
                        .param("key", "nonExistingKey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Key not found"));

        // Verify that the service's get method was called with the expected key
        verify(synchronizedCacheService, times(1)).get("nonExistingKey");
    }

    @Test
    void testRemoveEntryFromCache() throws Exception {
        // Simulate a DELETE request to remove a cache entry
        mockMvc.perform(delete("/cache/remove")
                        .param("key", "testKey")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Removed testKey"));

        // Verify that the service's remove method was called with the expected key
        verify(synchronizedCacheService, times(1)).remove("testKey");
    }

    @Test
    void testClearCache() throws Exception {
        // Simulate a DELETE request to clear the cache
        mockMvc.perform(delete("/cache/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Cache cleared"));

        // Verify that the service's clear method was called once
        verify(synchronizedCacheService, times(1)).clear();
    }
}
