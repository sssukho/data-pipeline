package com.sssukho.kafkaproducer.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditStatistics {
    @JsonProperty(value = "log_date")
    Long logDate;
    @JsonProperty(value = "user_id")
    String userId;
    @JsonProperty(value = "user_name")
    String userName;
    @JsonProperty(value = "event_type")
    String eventType;
    @JsonProperty(value = "application_name")
    String application;
    String ip;
    String location;
    @JsonProperty(value = "event_result")
    String eventResult;
}
