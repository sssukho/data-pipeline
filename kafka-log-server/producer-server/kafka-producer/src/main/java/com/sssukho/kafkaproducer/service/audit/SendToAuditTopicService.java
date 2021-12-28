package com.sssukho.kafkaproducer.service.audit;

import com.sssukho.kafkaproducer.dto.ProcessResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        sendToLogTopic(processResult.getLogData());
    }

    private void sendToLogTopic(List<Map<String, Object>> dataForLog) {
        kafkaTemplate.send(TOPIC_AUDIT_LOG, dataForLog);
    }

    private void sendToStatisticsTopic(List<Map<String, Object>> dataForStatistics) {
        kafkaTemplate.send(TOPIC_AUDIT_STATISTICS, dataForStatistics);
    }
}
