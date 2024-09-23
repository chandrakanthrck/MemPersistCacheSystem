CREATE TABLE cache_entry (
    cache_key VARCHAR(255) NOT NULL,          -- Primary key for the cache entry
    cache_value TEXT NOT NULL,                -- Value of the cache entry
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- When the entry was created
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Last access time of the entry
    access_count INT DEFAULT 0,               -- Count of how many times the entry has been accessed
    ttl TIMESTAMP NULL DEFAULT NULL,          -- Time to live, expiry for the cache entry
    PRIMARY KEY (cache_key),                  -- Primary key is cache_key
    INDEX idx_ttl (ttl)                       -- Index to optimize TTL-based queries
) ENGINE=InnoDB;
