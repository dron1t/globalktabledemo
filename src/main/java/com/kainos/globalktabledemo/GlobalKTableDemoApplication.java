package com.kainos.globalktabledemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
@EnableKafka
public class GlobalKTableDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlobalKTableDemoApplication.class, args);
    }

}
