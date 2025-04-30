package com.example.course_service.controller;

import com.example.course_service.dto.CourseRequest;
import com.example.course_service.dto.CourseResponse;
import com.example.course_service.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseResponse createCourse(@RequestHeader("Authorization") String authorizationHeader,
                                       @RequestBody CourseRequest request) {
        return courseService.createCourse(request, authorizationHeader);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(@PathVariable Long id,
                                       @RequestHeader("Authorization") String authorizationHeader,
                                       @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request, authorizationHeader);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id,
                             @RequestHeader("Authorization") String authorizationHeader) {
        courseService.deleteCourse(id, authorizationHeader);
    }
}
