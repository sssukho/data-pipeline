package com.sssukho.disruptorlog.dto;

import com.lmax.disruptor.EventFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

public class LogEvent {
    long currentTime;
    Object logString;
    DeferredResult<ResponseEntity> output;

    public LogEvent(long currentTime) {
        this.currentTime = currentTime;
    }

    public void set(Object logString, DeferredResult<ResponseEntity> output) {
        this.logString = logString;
        this.output = output;
    }

    public Object getLogString() {
        return logString;
    }

    public void setSuccess() {
        this.output.setResult(ResponseEntity.ok("OKAY"));
    }

    public void clear() {
        this.logString = null;
        this.output = null;
    }

    private static class LogEventFactory implements EventFactory<LogEvent> {
        @Override
        public LogEvent newInstance() {
            return new LogEvent(System.currentTimeMillis());
        }
    }

    public static final LogEventFactory FACTORY = new LogEventFactory();
}
