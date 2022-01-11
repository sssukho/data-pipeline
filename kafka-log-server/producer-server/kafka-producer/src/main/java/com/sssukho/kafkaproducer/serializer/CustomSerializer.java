package com.sssukho.kafkaproducer.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class CustomSerializer implements Serializer {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void configure(Map configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Object data) {
        try {
            if(data == null) {
                log.error("Null received at serializing");
                return null;
            }
            return mapper.writeValueAsBytes(data);
        } catch(Exception e) {
            throw new SerializationException("Error occurred when serializing");
        }
    }

    @Override
    public void close() {

    }

    @Override
    public byte[] serialize(String s, Object o) {

        return new byte[0];
    }
}
