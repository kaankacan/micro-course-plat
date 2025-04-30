package com.example.purchase_service.client;

import com.example.purchase_service.dto.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service")
public interface CourseClient {

    @GetMapping("/api/courses/{courseId}")
    CourseResponse getCourseById(@PathVariable("courseId") Long courseId);
}
