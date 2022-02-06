package com.sssukho.kafkaconsumerlog.dao;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name="audit_log")
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogEntity {
    @Id
    @Column(name="request_id")
    private String requestId;

    @Column(name="log_date")
    private Long logDate;

    @Column(name="user_id")
    private String userId;

    @Column(name="user_name")
    private String userName;

    @Column(name="user_type")
    private String userType;

    @Column(name="event_type")
    private String eventType;

    @Column(name="application_id")
    private String applicationId;

    @Column(name="application_name")
    private String applicationName;

    @Column(name="ip")
    private String ip;

    @Column(name="location")
    private String location;

    @Column(name="user_agent")
    private String userAgent;

    @Column(name="event_result")
    private String eventResult;
}
