package com.sssukho.disruptorlog.controller;

import com.sssukho.disruptorlog.service.DataProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Locale;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataProcessingService dataProcessingService;

    @RequestMapping(value = {"/v1/trace"}, method = RequestMethod.POST)
    public @ResponseBody DeferredResult<ResponseEntity> trace(@RequestBody Map logMap, Locale locale) {
        DeferredResult<ResponseEntity> output = new DeferredResult<>();

        dataProcessingService.process(logMap, output);

        return output;
    }
}
