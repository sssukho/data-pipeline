package com.sssukho.kafkaconsumerlog.bean;

import com.sssukho.kafkaconsumerlog.config.KafkaConsumerConfig;
import com.sssukho.kafkaconsumerlog.dto.AuditData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfigBean {

    @Autowired
    KafkaConsumerConfig kafkaConsumerConfig;

    @Bean
    public ConsumerFactory<String, AuditData> auditDataConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getCommonProducerConfig());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AuditData> auditDataConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AuditData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(auditDataConsumerFactory());
        return factory;
    }

    private Map<String, Object> getCommonProducerConfig() {
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfig.getBootStrapServer());
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getKeySerializer());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getValueSerializer());

        return consumerConfig;
    }


}
