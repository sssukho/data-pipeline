package com.sssukho.kafkaproducer.service.kafka;

import com.sssukho.kafkaproducer.dto.audit.AuditDashboard;
import com.sssukho.kafkaproducer.dto.audit.AuditData;
import com.sssukho.kafkaproducer.dto.audit.AuditStatistics;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class KafkaTemplatesByTopic {
    private final KafkaTemplate<String, AuditData> auditDataProducer;
    private final KafkaTemplate<String, AuditDashboard> auditDashboardProducer;
    private final KafkaTemplate<String, AuditStatistics> auditStatisticsProducer;
}
