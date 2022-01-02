package com.sssukho.kafkaproducer.service;

import com.sssukho.kafkaproducer.dto.PreProcessedData;
import com.sssukho.kafkaproducer.dto.ResponseMessageDTO;
import com.sssukho.kafkaproducer.dto.audit.AuditDashboard;
import com.sssukho.kafkaproducer.dto.audit.AuditData;
import com.sssukho.kafkaproducer.dto.audit.AuditStatistics;
import com.sssukho.kafkaproducer.exception.PushToKafkaException;
import com.sssukho.kafkaproducer.service.kafka.KafkaTemplatesByTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendToKafkaService<T> {

    private static final String TOPIC_AUDIT_LOG = "AUDIT_LOG";
    private static final String TOPIC_AUDIT_DASHBOARD = "AUDIT_DASHBOARD";
    private static final String TOPIC_AUDIT_STATISTICS = "AUDIT_STATISTICS";

    private static final String MESSAGE_TYPE_AUDIT = "AUDIT";
    private static final String MESSAGE_TYPE_SYSTEM = "SYSTEM";

    @Autowired
    KafkaTemplatesByTopic kafkaTemplates;

    public void send(String type, PreProcessedData preProcessedData) {
        switch(type) {
            case MESSAGE_TYPE_AUDIT:
                sendToAuditTopics(preProcessedData);
                break;

            case MESSAGE_TYPE_SYSTEM:
//                sendToSystemTopics(preProcessedData);
                break;
        }
    }

    private void sendToAuditTopics(PreProcessedData preProcessedData) {

        try {
            kafkaTemplates.getAuditDataProducer().send(
                    TOPIC_AUDIT_LOG, (AuditData) preProcessedData.getLogData());

            kafkaTemplates.getAuditStatisticsProducer().send(
                    TOPIC_AUDIT_STATISTICS, (AuditStatistics) preProcessedData.getStatisticsData());
        } catch(Exception e) {
            log.error("", e);
            preProcessedData.setResponseMessageDTO(new ResponseMessageDTO(e.getLocalizedMessage(), 500));
            return;

        }

        preProcessedData.setResponseMessageDTO(
                new ResponseMessageDTO("Complete to send kafka", 200));
    }
}
