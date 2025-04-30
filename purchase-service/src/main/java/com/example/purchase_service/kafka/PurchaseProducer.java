package com.example.purchase_service.kafka;

import com.example.purchase_service.kafka.event.PurchaseCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PurchaseProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PurchaseProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPurchaseCreatedEvent(PurchaseCreatedEvent event) {
        kafkaTemplate.send("purchase-created20", event);
    }
}
