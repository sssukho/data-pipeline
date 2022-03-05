package com.sssukho.kafkaproducer.service.refine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sssukho.kafkaproducer.dto.PreProcessedData;
import com.sssukho.kafkaproducer.dto.audit.AuditData;
import com.sssukho.kafkaproducer.dto.audit.AuditStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RefineAuditDataService {

    private final ObjectMapper mapper = new ObjectMapper();

    public void refine(PreProcessedData dataToProcess) {
        refineForLog(dataToProcess);
//        refineForDashboard(dataToProcess);
        refineForStatistics(dataToProcess);
    }

    private void refineForLog(PreProcessedData preProcessedData){
        AuditData rawAuditData = mapper.convertValue(preProcessedData.getLogData(), AuditData.class);
        preProcessedData.setLogData(rawAuditData);
    }

    private void refineForDashboard(PreProcessedData preProcessedData) {

    }

    private void refineForStatistics(PreProcessedData preProcessedData) {
        AuditData refinedAuditLog  = (AuditData) preProcessedData.getLogData();
        AuditStatistics auditStatisticsData = new AuditStatistics();

        auditStatisticsData.setLogDate(refinedAuditLog.getLogDate());
        auditStatisticsData.setUserId(refinedAuditLog.getUserId());
        auditStatisticsData.setUserName(refinedAuditLog.getUserName());
        auditStatisticsData.setEventType(refinedAuditLog.getEventType());
        auditStatisticsData.setApplication(refinedAuditLog.getApplicationId());
        auditStatisticsData.setIp(refinedAuditLog.getIp());
        auditStatisticsData.setLocation(refinedAuditLog.getLocation());
        auditStatisticsData.setEventResult(refinedAuditLog.getEventResult());

        preProcessedData.setStatisticsData(auditStatisticsData);
    }
}
