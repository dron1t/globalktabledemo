package com.kainos.cmd.producer;

import com.kainos.globalktabledemo.CacheConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

import static com.kainos.globalktabledemo.Config.CONFIG_TOPIC_NAME;

public class ProducerDemo {
    private static final Logger log = LoggerFactory.getLogger(ProducerDemo.class);

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        String bootstrapServers = "localhost:9092";

        // create Producer properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        // create the producer
        KafkaProducer<String, CacheConfig> producer = new KafkaProducer<>(properties);

        // create a producer record
        ProducerRecord<String, CacheConfig> producerRecord =
                new ProducerRecord<>(CONFIG_TOPIC_NAME,
                        "tech3camp",
                        new CacheConfig("key3",
                                "tech3Camp",
                                "@ Metropolia",
                                4));

        // send data - asynchronous
        producer.send(producerRecord);

        // flush data - synchronous
        producer.flush();
        // flush and close producer
        producer.close();
        System.out.println("Message send");
    }
}