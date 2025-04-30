package com.example.course_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCourseException extends RuntimeException {
    public InvalidCourseException(String message) {
        super(message);
    }
}
