package com.sssukho.kafkaproducer.service;

import com.sssukho.kafkaproducer.dto.ProcessResultDTO;
import com.sssukho.kafkaproducer.service.audit.SendToAuditTopicService;
import com.sssukho.kafkaproducer.service.audit.RefineAuditDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class ProcessToKafkaService {

    private static final String AUDIT = "audit";

    @Autowired
    RefineAuditDataService refineAuditDataService;
    @Autowired
    SendToAuditTopicService sendToAuditTopicService;

    public ProcessResultDTO process(ProcessResultDTO processResult) {

        String logType = (String) ((HashMap) processResult.getRequestMap()).get("type");

        // refine
        switch(logType) {
            case AUDIT:
                refineAuditDataService.refine(processResult);
                break;
        }

        // refine 하다가 에러 발생한 경우 (데이터가 적절하지 못한 경우)
        if(!processResult.getResopnseEntity().getStatusCode().is2xxSuccessful()) {
            return processResult;
        }

        // push to kafka broker
        switch(logType) {
            case AUDIT:
                sendToAuditTopicService.send(processResult);
                break;
        }

        return processResult;
    }


}
