package com.sssukho.disruptorlog.service;

import com.sssukho.disruptorlog.constant.ErrorType;
import com.sssukho.disruptorlog.constant.ParsingKeys;
import com.sssukho.disruptorlog.meta.TraceMetaInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorCheckService {

    public ResponseEntity traceProtocolCheck(HashMap traceMap, Map<String, TraceMetaInfo> queryMap) {

        return null;
    }

    public ResponseEntity queryProtocolCheck(HashMap queryMap) {

        return null;
    }


    public ResponseEntity protocolCheck(HashMap requestMap, Map<String, TraceMetaInfo> queryMap) {
        HashMap header = (HashMap) requestMap.get(ParsingKeys.REQUEST_MESSAGE_HEADER);
        if(header == null) {
            writeErrorLog(ErrorType.NOT_FOUND_HEADER.toString(), requestMap);
            return ResponseEntity.badRequest().body(responseBody(ErrorType.NOT_FOUND_HEADER));
        }

        Object type = header.get(ParsingKeys.REQUEST_MESSAGE_HEADER_TYPE);
        if(type == null) {
            writeErrorLog(ErrorType.NOT_FOUND_TYPE.toString(), requestMap);
//            return ResponseEntity.badRequest().body(responseBody(ErrorType.NOT_FOUND_TYPE));
        }
        if(type instanceof String == false) {
            writeErrorLog(ErrorType.HEADER_TYPE_INVALID_DATA_TYPE.toString(), requestMap);
//            return ResponseEntity.badRequest().body(responseBody(ErrorType.HEADER_TYPE_INVALID_DATA_TYPE));
        }

        Object version = header.get(ParsingKeys.REQUEST_MESSAGE_HEADER_VERSION);
        if(version == null) {
            writeErrorLog(ErrorType.NOT_FOUND_VERSION.toString(), requestMap);
//            return ResponseEntity.badRequest().body(responseBody(Error))
        }

        TraceMetaInfo info = queryMap.get(type);
        if(info == null) {
            writeErrorLog(ErrorType.NOT_FOUND_META_INFO.toString(), requestMap);
        }

        Object body = requestMap.get(ParsingKeys.REQUEST_MESSAGE_BODY);
        if(body == null) {
            writeErrorLog(ErrorType.NOT_FOUND_BODY.toString(), requestMap);
        }

        return null;
    }

    private Map<String, String> responseBody(ErrorType errorType) {
        Map responseBodyMap = new HashMap<String, String>();

        if(errorType.getHttpStatus().is4xxClientError()) {
            responseBodyMap.put(ParsingKeys.RESPONSE_MESSAGE_ERROR_CODE, "400");
        } else if(errorType.getHttpStatus().is5xxServerError()) {
            responseBodyMap.put(ParsingKeys.RESPONSE_MESSAGE_ERROR_CODE, "500");
        }

        responseBodyMap.put(ParsingKeys.RESPONSE_MESSAGE_ERROR_MESSAGE, errorType.toString());

        return responseBodyMap;
    }

    private void writeErrorLog(String errorMessage, HashMap requestMap) {
        log.error("{} : {}", errorMessage, requestMap);
    }
}
