package com.sssukho.kafkaproducer.controller;

import com.sssukho.kafkaproducer.dto.RequestMessageDTO;
import com.sssukho.kafkaproducer.dto.ResponseMessageDTO;
import com.sssukho.kafkaproducer.service.ProcessToKafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final ProcessToKafkaService processToKafkaService;

    @RequestMapping(value = "/v1/trace", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseMessageDTO trace(@RequestBody RequestMessageDTO requestMessageDTO) {

        return processToKafkaService.process(requestMessageDTO);
    }
}