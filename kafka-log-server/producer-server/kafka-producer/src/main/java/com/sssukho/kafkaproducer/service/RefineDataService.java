package com.sssukho.kafkaproducer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefineDataService {

    private final PushToKafkaService pushService;

    /**
     * 클라이언트로부터 넘어온 데이터들에서 필요한 정보만 빼서 각각의 Topic 으로 넘기기 위한 메소드
     *
     * @param requestData data from client
     * @return ResponseEntity
     */
    public ResponseEntity refineData(Map<String, Object> requestData) {
        // TODO @sssukho: 어떤 데이터들을 log topic과 statistics topic 에 나눠 넣어야 할지 요청 데이터 템플릿부터 정해야 함.
        // TODO @sssukho: Response 객체 DTO 하나 만들어서 그 안에 ResponseEntity 와 넘겨줘야 할 객체들 정의해서 쓸 수 있도록 수정 필요

        ResponseEntity processResultForLog = refineDataForLog(requestData);
        if(!processResultForLog.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.internalServerError().body("Refining data for log has failed");
        }

        ResponseEntity processResultForStatistics = refineDataForStatistics(requestData);
        if(!processResultForLog.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.internalServerError().body("Refining data for statistics has failed");
        }

        return ResponseEntity.ok("OKAY");
    }

    /**
     * 클라이언트에서 넘겨주는 모든 로그 데이터들에 대해 조회하기 위한 처리
     * 현재 정해진 사양이 없으므로, 클라이언트에서 넘어온 데이터 그대로 카프카에 푸쉬함
     *
     * @param requestMap data from client
     * @return ResponseEntity
     */
    private ResponseEntity refineDataForLog(Map<String, Object> requestMap) {

        Map<String, Object> refinedData = new HashMap<>();


        return ResponseEntity.ok("OKAY");
    }

    /**
     * 통계 처리를 위한 항목만 뺴서 kafka에 push
     *
     * @param requestData data from client
     * @return ResponseEntity
     */
    private ResponseEntity refineDataForStatistics(Map<String, Object> requestData) {

        HashMap<String, Object> header = (HashMap) requestData.get("header");
        String logType = (String) header.get("type");

        Map<String, Object> statisticsData = null;

        // 감사 로그인 경우
        if(logType.equals("audit_log")) {
            statisticsData = refineDataForAuditLog(requestData);
        }
        // 시스템 로그인 경우
        if(logType.equals("system_log")) {
            statisticsData = refineDataForSystemLog(requestData);
        }

        if(statisticsData == null) {
            return ResponseEntity.internalServerError().body("Data for statistics is invalid");
        }

        ResponseEntity pushResult = pushService.pushToLogTopic(statisticsData);
        if(!pushResult.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.internalServerError().body("Error has occurred while pushing to kafka. Check kafka server");
        }

        return ResponseEntity.ok("OKAY");
    }


    private Map refineDataForAuditLog(Map<String, Object> unRefinedLog) {
        HashMap<String, Object> refinedAuditLog = new HashMap<>();

        return refinedAuditLog;
    }

    private Map refineDataForSystemLog(Map<String, Object> unRefinedLog) {
        HashMap<String, Object> refinedSystemLog = new HashMap<>();

        return refinedSystemLog;
    }
}
