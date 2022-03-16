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
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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

            ListenableFuture<SendResult<String, AuditData>> auditDataFuture =
                    auditDataKafkaTemplate.send(Topic.AUDIT_LOG, (AuditData) preProcessedData.getLogData());

            auditDataFuture.addCallback(new ListenableFutureCallback<SendResult<String, AuditData>>() {
                @Override
                public void onFailure(Throwable ex) {
                    onFailureErrorMessage(ex);
                }

                @Override
                public void onSuccess(SendResult<String, AuditData> result) {
                    onSuccessMessage(result);
                }
            });

            ListenableFuture<SendResult<String, AuditStatistics>> auditStatisticsFuture =
                    auditStatisticsKafkaTemplate.send(Topic.AUDIT_STATISTICS, (AuditStatistics) preProcessedData.getStatisticsData());

            auditStatisticsFuture.addCallback(new ListenableFutureCallback<SendResult<String, AuditStatistics>>() {
                @Override
                public void onFailure(Throwable ex) {
                    onFailureErrorMessage(ex);
                }

                @Override
                public void onSuccess(SendResult<String, AuditStatistics> result) {
                    onSuccessMessage(result);
                }
            });

//            auditDataKafkaTemplate.send(Topic.AUDIT_LOG, (AuditData) preProcessedData.getLogData());
//            auditStatisticsKafkaTemplate.send(Topic.AUDIT_STATISTICS, (AuditStatistics) preProcessedData.getStatisticsData());
        } catch(Exception e) {
            log.error("", e);
            preProcessedData.setResponseMessageDTO(new ResponseMessageDTO(e.getLocalizedMessage(), 500));
            return;
        }

        preProcessedData.setResponseMessageDTO(
                new ResponseMessageDTO("Complete to send kafka", 200));
    }


    private void onFailureErrorMessage(Throwable ex) {
        log.error("Unable to send message: {}", ex.getMessage());
    }

    private void onSuccessMessage(SendResult sendResult) {
        log.info("Sent message with offset: {}", sendResult.getRecordMetadata().offset());
    }
}
