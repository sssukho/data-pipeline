package com.sssukho.kafkaproducer.service;

import com.sssukho.kafkaproducer.dto.PreProcessedData;
import com.sssukho.kafkaproducer.dto.RequestMessageDTO;
import com.sssukho.kafkaproducer.dto.ResponseMessageDTO;
import com.sssukho.kafkaproducer.exception.PushToKafkaException;
import com.sssukho.kafkaproducer.service.refine.RefineAuditDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessToKafkaService {

    private static final String MESSAGE_TYPE_AUDIT = "AUDIT";
    private static final String MESSAGE_TYPE_SYSTEM = "SYSTEM";

    private final RefineAuditDataService refineAuditDataService;
    private final SendToKafkaService sendToKafkaService;

    public ResponseMessageDTO process(RequestMessageDTO requestMessageDTO) {

        String type = requestMessageDTO.getType();
        PreProcessedData preProcessedData = new PreProcessedData();

        // set rawData
        preProcessedData.setLogData(requestMessageDTO.getData());

        // refine
        switch(type.toUpperCase()) {
            case MESSAGE_TYPE_AUDIT:
                refineAuditDataService.refine(preProcessedData);
                break;

            case MESSAGE_TYPE_SYSTEM:
//                refineSystemDataService.refine(preProcessedData);
                break;

            default:
                // type에 따라 byte 형태로 kafka send
                break;
        }

        // send to kafka
        sendToKafkaService.send(type.toUpperCase(), preProcessedData);

        return preProcessedData.getResponseMessageDTO();
    }
}
