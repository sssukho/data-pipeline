package com.sssukho.kafkaproducer.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PushToKafkaService {

    public ResponseEntity pushToLogTopic(Map<String, Object> requestData) {


        return ResponseEntity.ok("OKAY");
    }

    public ResponseEntity pushToStatisticsTopic(Map<String, Object> requestData) {


        return ResponseEntity.ok("OK");
    }

}
