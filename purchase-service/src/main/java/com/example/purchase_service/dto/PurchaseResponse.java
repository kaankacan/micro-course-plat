package com.example.purchase_service.dto;

import java.time.LocalDateTime;

public class PurchaseResponse {

    private Long purchaseId;
    private Long userId;
    private CourseResponse course;
    private LocalDateTime purchaseDate;

    public PurchaseResponse() {
    }

    public PurchaseResponse(Long purchaseId, Long userId, CourseResponse course, LocalDateTime purchaseDate) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.course = course;
        this.purchaseDate = purchaseDate;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
