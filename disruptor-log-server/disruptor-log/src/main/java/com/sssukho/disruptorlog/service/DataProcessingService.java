package com.sssukho.disruptorlog.service;

import com.sssukho.disruptorlog.constant.ParsingKeys;
import com.sssukho.disruptorlog.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataProcessingService {

    private final MetaDataService metaDataService;

    public DeferredResult<ResponseEntity> process(Map requestMap, DeferredResult<ResponseEntity> output) {
        // validation check
        ResponseEntity validationResult = validateRequestMessage(requestMap);
        if(validationResult.getStatusCode() == HttpStatus.BAD_REQUEST) {
            output.setResult(validationResult);
            return output;
        }

        DisruptorService disruptor;
        String type =
                (String) ((HashMap) requestMap.get(ParsingKeys.REQUEST_MESSAGE_HEADER)).get(ParsingKeys.REQUEST_MESSAGE_HEADER_TYPE);

        disruptor = metaDataService.dsMap.get(type);
        if(disruptor == null) {
            output.setResult(ResponseEntity.internalServerError().body("meta data doesn't exist"));
            return output;
        }

        List<Map> logDataList = (List) requestMap.get("body");
        for(Map logData : logDataList) {
            logData.put(ParsingKeys.LOG_ID, TimeUtils.generateUUID());
        }

        disruptor.event(requestMap, output);
        return output;
    }

    private ResponseEntity validateRequestMessage(Map logMap) {
        try {
            HashMap header = (HashMap) logMap.get(ParsingKeys.REQUEST_MESSAGE_HEADER);
            String type = (String) header.get(ParsingKeys.REQUEST_MESSAGE_HEADER_TYPE);
            String version = (String) header.get(ParsingKeys.REQUEST_MESSAGE_HEADER_VERSION);
            List<HashMap> listBody = (List<HashMap>) logMap.get(ParsingKeys.REQUEST_MESSAGE_BODY);

            // body에 아무런 데이터가 없는 경우에도
            if(listBody.size() == 0 || type == null || version == null)
                throw new Exception();

        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("invalid protocol");
        }

        return ResponseEntity.ok("OKAY");
    }
}
