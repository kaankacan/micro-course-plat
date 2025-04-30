package com.example.course_service.service;

import com.example.course_service.authorize.AuthorizationService;
import com.example.course_service.dto.CourseRequest;
import com.example.course_service.dto.CourseResponse;
import com.example.course_service.entity.Course;
import com.example.course_service.exception.CourseNotFoundException;
import com.example.course_service.exception.InvalidCourseException;
import com.example.course_service.kafka.CourseProducer;
import com.example.course_service.kafka.event.CourseCreateEvent;
import com.example.course_service.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final CourseProducer courseProducer;
    private final AuthorizationService authorizationService;

    public CourseService(CourseRepository courseRepository, CourseProducer courseProducer, AuthorizationService authorizationService) {
        this.courseRepository = courseRepository;
        this.courseProducer = courseProducer;
        this.authorizationService = authorizationService;
    }

    public List<CourseResponse> getAllCourses() {
        log.info("Listing all courses.");
        return courseRepository.findAll().stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long id) {
        log.info("Searching course by ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found. ID: {}", id);
                    return new CourseNotFoundException("Course not found. ID: " + id);
                });
        return mapToCourseResponse(course);
    }

    public CourseResponse createCourse(CourseRequest request, String authorizationHeader) {
        log.info("Creating a new course: {}", request.getTitle());
        String token = extractToken(authorizationHeader);
        authorizationService.checkAdminRole(token);

        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            log.error("Course creation failed. Title is missing.");
            throw new InvalidCourseException("Course title is required.");
        }

        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setInstructor(request.getInstructor());

        Course savedCourse = courseRepository.save(course);
        log.info("New course created. ID: {}", savedCourse.getId());

        CourseCreateEvent event = new CourseCreateEvent(
                course.getId(),
                course.getPrice(),
                course.getTitle(),
                course.getInstructor(),
                LocalDateTime.now().toString()
        );
        courseProducer.sendCourseCreateEvent(event);

        return mapToCourseResponse(savedCourse);
    }

    public CourseResponse updateCourse(Long id, CourseRequest request, String authorizationHeader) {
        log.info("Updating course. ID: {}", id);
        String token = extractToken(authorizationHeader);
        authorizationService.checkAdminRole(token);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found for updating. ID: {}", id);
                    return new CourseNotFoundException("Course not found. ID: " + id);
                });

        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            log.error("Course update failed. Title is missing. ID: {}", id);
            throw new InvalidCourseException("Course title is required.");
        }

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setInstructor(request.getInstructor());

        Course updatedCourse = courseRepository.save(course);
        log.info("Course updated. ID: {}", updatedCourse.getId());

        return mapToCourseResponse(updatedCourse);
    }

    public void deleteCourse(Long id, String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        authorizationService.checkAdminRole(token);
        log.info("Deleting course. ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found for deletion. ID: {}", id);
                    return new CourseNotFoundException("Course not found. ID: " + id);
                });

        courseRepository.delete(course);
        log.info("Course deleted. ID: {}", id);
    }

    private CourseResponse mapToCourseResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPrice(),
                course.getInstructor()
        );
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new RuntimeException("Authorization header is missing or invalid.");
        }
    }
}
