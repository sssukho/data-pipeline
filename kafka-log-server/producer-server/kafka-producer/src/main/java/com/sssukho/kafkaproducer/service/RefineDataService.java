package com.sssukho.kafkaproducer.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RefineDataService {

    public Map<String, Object> refineDataForLog(Map<String, Object> requestMap) {
        // consumer-log
        Map<String, Object> refinedData = new HashMap<>();


        return refinedData;
    }

    public Map<String, Object> refineDataForDashboard(Map<String, Object> requestMap) {
        // consumer-dashboard
        Map<String, Object> refinedData = new HashMap<>();


        return refinedData;
    }
}
