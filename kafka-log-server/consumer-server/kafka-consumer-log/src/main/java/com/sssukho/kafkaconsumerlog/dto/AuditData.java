package com.sssukho.kafkaconsumerlog.dto;

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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("logDate: " + logDate + "\n");
        sb.append("requestId: " + requestId + "\n");
        sb.append("userId: " + userId + "\n");
        sb.append("userName: " + userName + "\n");
        sb.append("userType: " + userType + "\n");
        sb.append("eventType: " + eventType + "\n");
        sb.append("applicationId: " + applicationId + "\n");
        sb.append("applicationName: " + applicationName + "\n");
        sb.append("ip: " + ip + "\n");
        sb.append("location: " + location + "\n");
        sb.append("userAgent: " + userAgent + "\n");
        sb.append("eventResult: " + eventResult + "\n");

        return sb.toString();
    }
}
