package com.example.history_migration.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class IdService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable("minId")
    public Integer getMinId() {
        return jdbcTemplate.queryForObject("SELECT MIN(id) FROM My_entity", Integer.class);
    }

    @Cacheable("maxId")
    public Integer getMaxId() {
        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM My_entity", Integer.class);
    }
}
