package com.sssukho.disruptorlog.service;

import com.sssukho.disruptorlog.constant.ParsingKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataProcessingService {

    public DeferredResult<ResponseEntity> process(Map logMap, DeferredResult<ResponseEntity> output) {


        return output;
    }

    private ResponseEntity validateRequestMessage(Map logMap) {
        try {
            HashMap header = (HashMap) logMap.get(ParsingKeys.REQUEST_MESSAGE_HEADER);
            String type = (String) header.get(ParsingKeys.REQUEST_MESSAGE_HEADER_TYPE);
            String version = (String) header.get(ParsingKeys.REQUEST_MESSAGE_HEADER_VERSION);
            List<HashMap> listBody = (List<HashMap>) logMap.get(ParsingKeys.REQUEST_MESSAGE_BODY);

        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest();
        }

        ResponseEntity
        return ResponseEntity.ok("OKAY");
    }
}
