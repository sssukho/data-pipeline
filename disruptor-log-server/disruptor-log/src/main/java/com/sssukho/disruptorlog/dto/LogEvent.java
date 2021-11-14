package com.sssukho.disruptorlog.dto;

import com.lmax.disruptor.EventFactory;
import org.springframework.http.ResponseEntity;

public class LogEvent {
    long currentTime;
    Object logString;
    ResponseEntity responseEntity;

    public LogEvent(long currentTime) {
        this.currentTime = currentTime;
    }

    public void set(Object logString, ResponseEntity responseEntity) {
        this.logString = logString;
        this.responseEntity = responseEntity;
    }

    public Object getLogString() {
        return logString;
    }

    public void setSuccess() {
        this.responseEntity = ResponseEntity.ok("OKAY");
    }

    public void clear() {
        this.logString = null;
        this.responseEntity = null;
    }

    private static class LogEventFactory implements EventFactory<LogEvent> {
        @Override
        public LogEvent newInstance() {
            return new LogEvent(System.currentTimeMillis());
        }
    }

    public static final LogEventFactory FACTORY = new LogEventFactory();
}
