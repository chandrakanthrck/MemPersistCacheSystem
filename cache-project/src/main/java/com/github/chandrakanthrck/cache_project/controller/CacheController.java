package com.github.chandrakanthrck.cache_project.controller;

import com.github.chandrakanthrck.cache_project.eviction.CacheValue;
import com.github.chandrakanthrck.cache_project.service.SynchronizedCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final SynchronizedCacheService cacheService; // Use the non-generic version

    @Autowired
    public CacheController(SynchronizedCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping("/put")
    public ResponseEntity<String> put(@RequestParam String key, @RequestParam String value) {
        CacheValue<String> cacheValue = new CacheValue<>(value); // Create CacheValue
        cacheService.put(key, cacheValue); // Use CacheValue<String>
        return new ResponseEntity<>("Cached " + key, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> get(@RequestParam String key) {
        CacheValue<String> value = cacheService.get(key);
        if (value != null) {
            return new ResponseEntity<>(value.getValue(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Key not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam String key) {
        cacheService.remove(key);
        return new ResponseEntity<>("Removed " + key, HttpStatus.OK);
    }
}
