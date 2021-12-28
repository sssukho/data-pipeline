package com.sssukho.kafkaproducer.service.audit;

import com.sssukho.kafkaproducer.dto.ProcessResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RefineAuditDataService {

    public void refine(ProcessResultDTO processResult) {
        List rawDataList = null;

        try {
             rawDataList = (List<Map<String, Object>>) processResult.getRequestMap().get("data");
        } catch(Exception e) {
            log.error(" ", e);
            processResult.setResopnseEntity(ResponseEntity.badRequest().body("Invalid request data"));
            return;
        }

        refineToLogData(rawDataList, processResult);
        if(!processResult.getResopnseEntity().getStatusCode().is2xxSuccessful()) {
            return;
        }

        refineToStatisticsData(rawDataList, processResult);
        if(!processResult.getResopnseEntity().getStatusCode().is2xxSuccessful()) {
            return;
        }
    }

    private void refineToLogData(List rawDataList, ProcessResultDTO processResult) {
        processResult.setLogData(rawDataList);
        processResult.setResopnseEntity(ResponseEntity.ok("Refine to log data complete"));
    }

    private void refineToStatisticsData(List<Map<String, Object>> rawDataList, ProcessResultDTO processResult) {
        List refinedDataForStatistics = new ArrayList<Map<String, Object>>();

        try {
            for(Map<String, Object> rawData : rawDataList) {
                rawData.remove("request_id");
                rawData.remove("user_agent");
                rawData.remove("user_type");
                rawData.remove("application_id");

                refinedDataForStatistics.add(rawData);
            }
        } catch(Exception e) {
            log.error(" ", e);
            processResult.setResopnseEntity(ResponseEntity.badRequest().body("Invalid data to refine statistics data"));
            return;
        }

        processResult.setStatisticsData(refinedDataForStatistics);
        processResult.setResopnseEntity(ResponseEntity.ok("refine to statistics data complete"));
    }
}
