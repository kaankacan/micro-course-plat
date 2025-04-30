package com.example.course_service.kafka;


import com.example.course_service.kafka.event.CourseCreateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CourseProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public CourseProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendCourseCreateEvent(CourseCreateEvent event)
    {
        kafkaTemplate.send("course-created",event);
    }
}
