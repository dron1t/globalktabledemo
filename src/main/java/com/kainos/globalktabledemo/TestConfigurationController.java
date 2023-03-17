package com.kainos.globalktabledemo;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.kainos.globalktabledemo.Config.CONFIG_STORE;
import static org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore;

@RestController
@RequiredArgsConstructor
public class TestConfigurationController {

    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;


    @GetMapping("/configuration/{key}")
    @Timed(value="testconfiguration.getconfiguration", description="Time to get configuration")
    public ResponseEntity getConfiguration(@PathVariable final String key) {
        final KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, CacheConfig> store = kafkaStreams.store(StoreQueryParameters.fromNameAndType(CONFIG_STORE, keyValueStore()));
        return ResponseEntity.of(Optional.ofNullable(store.get(key)));
    }
}
