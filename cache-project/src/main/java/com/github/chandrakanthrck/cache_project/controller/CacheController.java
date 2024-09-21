package com.github.chandrakanthrck.cache_project.controller;

import com.github.chandrakanthrck.cache_project.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cache")
public class CacheController {
    private final CacheService<String, Object> cacheService;

    @Autowired
    public CacheController(CacheService<String, Object> cacheService) {
        this.cacheService = cacheService;
    }
    @PostMapping("/put")
    public ResponseEntity<String> put(@RequestParam String key, @RequestParam String value){
        cacheService.put(key,value);
        return new ResponseEntity<>("Cached " + key, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> get(@RequestParam String key){
        Object value = cacheService.get(key);
        if(value!=null){
            return new ResponseEntity<>(value, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Key not found", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam String key){
        cacheService.remove(key);
        return new ResponseEntity<>("Removed " + key, HttpStatus.OK);
    }
}
