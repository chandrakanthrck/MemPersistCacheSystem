package com.github.chandrakanthrck.cache_project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cache_entries")
public class CacheEntry {
    public String key;
    public String value;
}
