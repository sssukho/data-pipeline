package com.sssukho.kafkaproducer.controller;

import com.sssukho.kafkaproducer.service.RefineDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LogController {

    private final RefineDataService refineDataService;

    @RequestMapping(value = "/v1/trace/audit", method = RequestMethod.POST)
    public HttpEntity trace(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                            @RequestParam Map<String, Object> requestMap) {

        HttpEntity processResult = refineDataService.refineData(requestMap);

        return processResult;
    }
}
