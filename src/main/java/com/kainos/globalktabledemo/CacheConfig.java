package com.kainos.globalktabledemo;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("CacheConfig")
public record CacheConfig(String id, String configFieldOne, String configFieldTwo, int identification) {

}
