package com.dmg.dataserver.business.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dmg.dataserver.business.model.User;

@Service
public class UserCacheService {
    @Cacheable(cacheNames = "platfrom_user", key = "#id", unless = "#result == null")
    public User getCacheUser(long id) {
        return null;
    }

    @CachePut(cacheNames = "platfrom_user", key = "#user.id", unless = "#result == null")
    public User update(User user) {
        return user;
    }
}