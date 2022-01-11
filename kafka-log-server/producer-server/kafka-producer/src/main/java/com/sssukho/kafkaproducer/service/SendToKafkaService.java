package com.sssukho.kafkaproducer.service;

import com.sssukho.kafkaproducer.constants.MessageType;
import com.sssukho.kafkaproducer.constants.Topic;
import com.sssukho.kafkaproducer.dto.PreProcessedData;
import com.sssukho.kafkaproducer.dto.ResponseMessageDTO;
import com.sssukho.kafkaproducer.dto.audit.AuditData;
import com.sssukho.kafkaproducer.dto.audit.AuditStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendToKafkaService<T> {

    private final KafkaTemplate<String, AuditData> auditDataKafkaTemplate;
    private final KafkaTemplate<String, AuditStatistics> auditStatisticsKafkaTemplate;

    public void send(String type, PreProcessedData preProcessedData) {
        switch(type) {
            case MessageType.AUDIT:
                sendToAuditTopics(preProcessedData);
                break;

            case MessageType.SYSTEM:
//                sendToSystemTopics(preProcessedData);
                break;
        }
    }

    private void sendToAuditTopics(PreProcessedData preProcessedData) {
        try {
            auditDataKafkaTemplate.send(Topic.AUDIT_LOG, (AuditData) preProcessedData.getLogData());
            auditStatisticsKafkaTemplate.send(Topic.AUDIT_STATISTICS, (AuditStatistics) preProcessedData.getStatisticsData());
        } catch(Exception e) {
            log.error("", e);
            preProcessedData.setResponseMessageDTO(new ResponseMessageDTO(e.getLocalizedMessage(), 500));
            return;
        }

        preProcessedData.setResponseMessageDTO(
                new ResponseMessageDTO("Complete to send kafka", 200));
    }
}
