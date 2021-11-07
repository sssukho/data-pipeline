package com.sssukho.kafkaproducer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@ConfigurationProperties(prefix = "kafka")
@Configuration
@Data
public class KafkaConfig {
    private String bootStrapServer;

}
