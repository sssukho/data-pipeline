package com.sssukho.kafkaconsumerlog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sssukho.kafkaconsumerlog.constants.Group;
import com.sssukho.kafkaconsumerlog.constants.Topic;
import com.sssukho.kafkaconsumerlog.dto.AuditData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumeFromKafkaService {

    private static ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = Topic.AUDIT_LOG, groupId = Group.AUDIT_LOG_GROUP, containerFactory = "auditDataConcurrentKafkaListenerContainerFactory")
    public void consume(AuditData auditData) {
        log.info("Consumed message : {}", auditData.toString());
    }

    @KafkaListener(topics = Topic.AUDIT_LOG, containerFactory = "auditDataConcurrentKafkaListenerContainerFactory")
    public void consume2(String message) {
        log.info(message);
    }

}
