//package com.github.chandrakanthrck.cache_project;
//
//import com.github.chandrakanthrck.cache_project.service.SynchronizedCacheService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class CacheProjectIntegrationTest {
//
//	@Autowired
//	private SynchronizedCacheService<String, Object> cacheService;
//
//	@Test
//	void testCacheAndPersistentStore() {
//		// Put value in cache and persistent store
//		cacheService.put("key1", "value1");
//
//		// Ensure the value is correctly stored and retrieved
//		assertEquals("value1", cacheService.get("key1"));
//
//		// Ensure the persistent store was also updated
//		assertNotNull(cacheService.get("key1"));
//	}
//}
