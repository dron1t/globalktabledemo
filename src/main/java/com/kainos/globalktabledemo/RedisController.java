package com.kainos.globalktabledemo;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RedisController {

   private final CacheConfigRepository cacheConfigRepository;

    @GetMapping("/redis/{keyValue}")
    @Timed(value="rediscontroller.getconfiguration", description="Time to get configuration from redis")
    public ResponseEntity<Optional<CacheConfig>> getConfig(@PathVariable String keyValue) {
        return ResponseEntity.ok().body(cacheConfigRepository.findById(keyValue));
    }
}