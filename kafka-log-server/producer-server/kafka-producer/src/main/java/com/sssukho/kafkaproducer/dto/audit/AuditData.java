package com.sssukho.kafkaproducer.dto.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditData {
    @JsonProperty(value="log_date")
    Long logDate;
    @JsonProperty(value="request_id")
    String requestId;
    @JsonProperty(value="user_id")
    String userId;
    @JsonProperty(value="user_name")
    String userName;
    @JsonProperty(value="user_type")
    String userType;
    @JsonProperty(value="event_type")
    String eventType;
    @JsonProperty(value="application_id")
    String applicationId;
    @JsonProperty(value="application_name")
    String applicationName;
    String ip;
    String location;
    @JsonProperty(value="user_agent")
    String userAgent;
    @JsonProperty(value="event_result")
    String eventResult;
}
