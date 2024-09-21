CREATE TABLE cache_store (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cache_key VARCHAR(255) NOT NULL UNIQUE,  -- Ensure keys are unique for fast lookups
    cache_value TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    access_count INT DEFAULT 0,
    ttl TIMESTAMP NULL DEFAULT NULL             -- Use TIMESTAMP to denote expiration or NULL if not applicable
);

CREATE INDEX idx_cache_key ON cache_store (cache_key);
