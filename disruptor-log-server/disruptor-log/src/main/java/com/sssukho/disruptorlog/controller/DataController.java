package com.sssukho.disruptorlog.controller;

import com.sssukho.disruptorlog.constant.ErrorType;
import com.sssukho.disruptorlog.service.DataProcessingService;
import com.sssukho.disruptorlog.service.QueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataController {

    private static final String CONTENT_TYPE_JSON = "content-type=application/json";

    private final DataProcessingService dataProcessingService;
    private final QueryService queryService;

    @RequestMapping(value = {"/v1/trace"}, method = RequestMethod.POST)
    public @ResponseBody DeferredResult<ResponseEntity> trace(@RequestBody Map logMap, Locale locale) {
        DeferredResult<ResponseEntity> output = new DeferredResult<>();

        dataProcessingService.process(logMap, output);

        return output;
    }

    @RequestMapping(value = {"/v1/query"}, method = RequestMethod.POST, headers = CONTENT_TYPE_JSON)
    public @ResponseBody ResponseEntity<?> query (@RequestBody HashMap logMap, Locale locale) {

        return queryService.query(logMap);
    }
}
