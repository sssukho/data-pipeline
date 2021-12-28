package com.sssukho.kafkaproducer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ProcessResultDTO {
    ResponseEntity resopnseEntity;
    Map<String, Object> requestMap;
    List<Map<String, Object>> logData;
    List<Map<String, Object>> statisticsData;
}
