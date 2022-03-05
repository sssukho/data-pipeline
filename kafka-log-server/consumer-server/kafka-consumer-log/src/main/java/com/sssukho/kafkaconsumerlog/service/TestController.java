package com.sssukho.kafkaconsumerlog.service;

import com.sssukho.kafkaconsumerlog.dao.AuditLogEntity;
import com.sssukho.kafkaconsumerlog.dto.AuditData;
import com.sssukho.kafkaconsumerlog.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {

    private final AuditLogRepository auditLogRepository;

    @RequestMapping(value = { "/v1/test"}, method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> test(@RequestBody AuditData auditData) {

        log.info("#### test controller called");

        ResponseEntity responseEntity = ResponseEntity.ok("OKAY");

        AuditLogEntity auditLogEntity =
                AuditLogEntity.builder()
                .logDate(auditData.getLogDate())
                .requestId(auditData.getRequestId())
                .userId(auditData.getUserId())
                .userName(auditData.getUserName())
                .userType(auditData.getUserType())
                .eventType(auditData.getEventType())
                .applicationId(auditData.getApplicationId())
                .applicationName(auditData.getApplicationName())
                .ip(auditData.getIp())
                .location(auditData.getLocation())
                .userAgent(auditData.getUserAgent())
                .eventResult(auditData.getEventResult())
                .build();

        auditLogRepository.save(auditLogEntity);

        return responseEntity;
    }
}