package com.kainos.globalktabledemo;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Configuration
public class Config {

    public static final String DEMO_CONFIG_NAME = "demo-config-name";
    public static final String CONFIG_STORE = "config-store";

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public GlobalKTable<String, CacheConfig> buildConfiguration(StreamsBuilder builder) {
        Serde<String> keySerde = Serdes.String();
        Serde<CacheConfig> valueSerde = new JsonSerde<>(CacheConfig.class);
        return builder.globalTable(DEMO_CONFIG_NAME, Consumed.with(keySerde, valueSerde),
                Materialized.<String, CacheConfig, KeyValueStore< Bytes, byte[]>>as(CONFIG_STORE)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(valueSerde)
                                        .withCachingEnabled());
    }

    @Component
    @RequiredArgsConstructor
    class Producer {

        private final KafkaTemplate<String, CacheConfig> kafkaTemplate;
        private final CacheConfigRepository cacheConfigRepository;

        @EventListener(ApplicationStartedEvent.class)
        public void produce() {
            kafkaTemplate.send(DEMO_CONFIG_NAME, "key1", new CacheConfig("key1","fieldOne", "fieldTwoTwo", 1));
            kafkaTemplate.send(DEMO_CONFIG_NAME, "key2", new CacheConfig("key2","otherKey", "fieldTwoThree", 2));
            kafkaTemplate.send(DEMO_CONFIG_NAME, "key1", new CacheConfig("key1","replacedKey", "fieldTwoThree", 2));

            cacheConfigRepository.save(new CacheConfig("key2","otherKey", "fieldTwoThree", 2));
            cacheConfigRepository.save(new CacheConfig("key1","replacedKey", "fieldTwoThree", 2));
        }
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration("redis", 6379));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

}
