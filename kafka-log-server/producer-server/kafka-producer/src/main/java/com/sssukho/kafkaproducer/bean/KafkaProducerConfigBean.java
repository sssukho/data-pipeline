package com.sssukho.kafkaproducer.bean;

import com.sssukho.kafkaproducer.config.KafkaProducerConfig;
import com.sssukho.kafkaproducer.dto.audit.AuditDashboard;
import com.sssukho.kafkaproducer.dto.audit.AuditData;
import com.sssukho.kafkaproducer.dto.audit.AuditStatistics;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * author           : sukholim
 */
@EnableKafka
@Configuration
public class KafkaProducerConfigBean {

    @Autowired
    KafkaProducerConfig kafkaProducerConfig;

    @Bean
    public ProducerFactory<String, AuditData> auditDataProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonProducerConfig());
    }

    @Bean
    public ProducerFactory<String, AuditStatistics> auditStatisticsProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonProducerConfig());
    }

    @Bean
    public ProducerFactory<String, AuditDashboard> auditDashboardProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, AuditData> auditDataKafkaTemplate() {
        return new KafkaTemplate<String, AuditData>(auditDataProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, AuditStatistics> auditStatisticsKafkaTemplate() {
        return new KafkaTemplate<String, AuditStatistics>(auditStatisticsProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, AuditDashboard> auditDashboardKafkaTemplate() {
        return new KafkaTemplate<String, AuditDashboard>(auditDashboardProducerFactory());
    }

    private Map<String, Object> getCommonProducerConfig() {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.getBootStrapServer());
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getKeySerializer());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getValueSerializer());
        return producerConfig;
    }


}