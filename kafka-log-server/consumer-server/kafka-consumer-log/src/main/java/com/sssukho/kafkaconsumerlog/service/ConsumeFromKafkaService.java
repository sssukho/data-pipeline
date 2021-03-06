package com.sssukho.kafkaconsumerlog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sssukho.kafkaconsumerlog.config.KafkaConsumerConfig;
import com.sssukho.kafkaconsumerlog.constants.Group;
import com.sssukho.kafkaconsumerlog.constants.Topic;
import com.sssukho.kafkaconsumerlog.dao.AuditLogEntity;
import com.sssukho.kafkaconsumerlog.dto.AuditData;
import com.sssukho.kafkaconsumerlog.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumeFromKafkaService {
    private static ObjectMapper mapper = new ObjectMapper();

    private final AuditLogRepository auditLogRepository;

    @KafkaListener(topics = Topic.AUDIT_LOG, groupId = Group.AUDIT_LOG_GROUP)
    public void listen(@Payload String auditDataStr) {
        ObjectMapper mapper = new ObjectMapper();
        AuditData auditData;
        try {
            auditData = mapper.readValue(auditDataStr, AuditData.class);
            log.info("Consumed message : {}", auditData.toString());
        } catch(Exception e) {
            log.error("", e);
            return;
        }

        AuditLogEntity auditLogEntity =
                AuditLogEntity.builder()
                        .logDate(auditData.getLogDate())
                        .requestId(auditData.getRequestId())
                        .userId(auditData.getUserId())
                        .userName(auditData.getUserName())
                        .userType(auditData.getUserType())
                        .eventType(auditData.getEventType())
                        .applicationId(auditData.getApplicationId())
                        .applicationName(auditData.getApplicationName())
                        .ip(auditData.getIp())
                        .location(auditData.getLocation())
                        .userAgent(auditData.getUserAgent())
                        .eventResult(auditData.getEventResult())
                        .build();

        auditLogRepository.save(auditLogEntity);
        log.info("audit data inserted into db");
    }
}
