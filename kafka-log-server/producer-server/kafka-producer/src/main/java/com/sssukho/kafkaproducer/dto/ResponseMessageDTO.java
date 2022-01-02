package com.sssukho.kafkaproducer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseMessageDTO {
    String responseMessage;
    Integer statusCode;
}
