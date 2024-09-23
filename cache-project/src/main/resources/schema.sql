CREATE TABLE cache_entry (
    cache_key VARCHAR(255) NOT NULL,          -- Renamed from 'key' to 'cache_key'
    cache_value TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    access_count INT DEFAULT 0,
    ttl TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (cache_key)
) ENGINE=InnoDB;
