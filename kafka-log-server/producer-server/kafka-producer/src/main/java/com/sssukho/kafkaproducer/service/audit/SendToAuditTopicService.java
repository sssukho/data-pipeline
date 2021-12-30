package com.sssukho.kafkaproducer.service.audit;

import com.sssukho.kafkaproducer.dto.ProcessResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SendToAuditTopicService {

    private static final String TOPIC_AUDIT_LOG = "AUDIT_LOG";
    private static final String TOPIC_AUDIT_STATISTICS = "AUDIT_STATISTICS";

    @Autowired
    KafkaTemplate<String, List<Map<String, Object>>> kafkaTemplate;

    public void send(ProcessResultDTO processResult) {
        try {
            kafkaTemplate.send(TOPIC_AUDIT_LOG, processResult.getLogData());
            kafkaTemplate.send(TOPIC_AUDIT_STATISTICS, processResult.getStatisticsData());
        } catch(Exception e) {
            log.error("", e);
            processResult.setResopnseEntity(ResponseEntity.internalServerError().body("Kafka broker service unavailable"));
            return;
        }

        processResult.setResopnseEntity(ResponseEntity.ok("OKAY"));
    }
}
