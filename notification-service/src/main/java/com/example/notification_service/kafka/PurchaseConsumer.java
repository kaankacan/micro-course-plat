package com.example.notification_service.kafka;

import com.example.notification_service.kafka.event.CourseCreateEvent;
import com.example.notification_service.kafka.event.PurchaseCreatedEvent;
import com.example.notification_service.kafka.event.UserRegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PurchaseConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseConsumer.class);

    @KafkaListener(topics = "purchase-created20", groupId = "notification-service-group")
    public void consumePurchase(PurchaseCreatedEvent event) {
        logger.info("Purchase event received: userId={}, courseId={}, purchaseDate={}",
                event.getUserId(), event.getCourseId(), event.getPurchaseDate());
    }

    @KafkaListener(topics = "user-register", groupId = "notification-service-group")
    public void consumeUser(UserRegisterEvent event) {
        logger.info("User registration event received: userId={}, useremail={}, userfullname={}",
                event.getId(), event.getEmail(), event.getFullName());
    }

    @KafkaListener(topics = "course-created", groupId = "notification-service-group")
    public void consumeCourse(CourseCreateEvent event) {
        logger.info("Course created event received: id={}, title={}, price={}, instructor={}, courseCreatedDate={}",
                event.getId(), event.getTitle(), event.getPrice(), event.getInstructor(), event.getCourseCreatedDate());
    }
}
