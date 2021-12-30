package com.sssukho.kafkaproducer.controller;

import com.sssukho.kafkaproducer.dto.ProcessResultDTO;
import com.sssukho.kafkaproducer.service.ProcessToKafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
public class LogController {

    @Autowired
    ProcessToKafkaService processToKafkaService;

    @RequestMapping(value = "/v1/trace", method = RequestMethod.POST)
    public ResponseEntity trace(@RequestBody Map<String, Object> requestMap) {

        ProcessResultDTO processResult = ProcessResultDTO.builder().requestMap(requestMap).build();
        processToKafkaService.process(processResult);

        return processResult.getResopnseEntity();
    }
}
