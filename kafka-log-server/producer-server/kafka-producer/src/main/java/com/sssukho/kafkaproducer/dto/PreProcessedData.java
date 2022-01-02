package com.sssukho.kafkaproducer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PreProcessedData<T> {
    ResponseMessageDTO responseMessageDTO;
    T logData;
    T dashboardData;
    T statisticsData;
}
