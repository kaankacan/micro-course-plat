package com.example.purchase_service.dto;

public class PurchaseRequest {

    private Long courseId;

    public PurchaseRequest() {
    }

    public PurchaseRequest( Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
