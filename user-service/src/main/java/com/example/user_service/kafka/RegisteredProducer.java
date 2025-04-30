package com.example.user_service.kafka;

import com.example.user_service.kafka.event.UserRegisterEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegisteredProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RegisteredProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void userRegisterEvent(UserRegisterEvent event) {
        kafkaTemplate.send("user-register", event);
    }
}


