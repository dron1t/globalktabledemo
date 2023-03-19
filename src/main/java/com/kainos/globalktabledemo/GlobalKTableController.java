package com.kainos.globalktabledemo;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.kainos.globalktabledemo.Config.CONFIG_STORE;
import static org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GlobalKTableController {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    private final LogTime logTime = new LogTime();


    @GetMapping("/gktable/{key}")
    @Timed(value = "globalktablecontroller.getconfiguration", description = "Time to get configuration from gktable")
    public ResponseEntity<CacheConfig> getConfiguration(@PathVariable final String key) {
        var value = logTime.produceLogged(() -> {
            final KafkaStreams kafkaStreams = logTime.produceLogged(() -> streamsBuilderFactoryBean.getKafkaStreams(), "getting kafka streams", log);
            ReadOnlyKeyValueStore<String, CacheConfig> store = logTime.produceLogged(() -> kafkaStreams.store(StoreQueryParameters.fromNameAndType(CONFIG_STORE, keyValueStore())), "Getting store", log);
            return logTime.produceLogged(() -> store.get(key), "getting value", log);
        }, "whole operation", log);
        return ok().body(value);
    }
}
