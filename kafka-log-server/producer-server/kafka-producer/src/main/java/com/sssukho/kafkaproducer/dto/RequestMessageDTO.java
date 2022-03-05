package com.sssukho.kafkaproducer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessageDTO <T>{
    String version;
    String type;
    Integer count;
    T data;
}
