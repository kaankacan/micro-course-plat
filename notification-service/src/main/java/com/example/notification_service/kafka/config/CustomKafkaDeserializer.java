package com.example.notification_service.kafka.config;

import com.example.notification_service.kafka.event.CourseCreateEvent;
import com.example.notification_service.kafka.event.PurchaseCreatedEvent;
import com.example.notification_service.kafka.event.UserRegisterEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class CustomKafkaDeserializer implements Deserializer<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }

            if (topic.equals("purchase-created20")) {
                return objectMapper.readValue(data, PurchaseCreatedEvent.class);
            } else if (topic.equals("user-register")) {
                return objectMapper.readValue(data, UserRegisterEvent.class);
            } else if (topic.equals("course-created")) {
                return objectMapper.readValue(data, CourseCreateEvent.class);
            } else {
                throw new IllegalArgumentException("Unknown topic received: " + topic);
            }
        } catch (Exception e) {
            throw new RuntimeException("Kafka deserialization error: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
    }
}
