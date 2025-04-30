package com.example.notification_service.kafka.event;

public class PurchaseCreatedEvent {
    private Long userId;
    private Long courseId;
    private String purchaseDate;

    public PurchaseCreatedEvent() {
    }

    public PurchaseCreatedEvent(Long userId, Long courseId, String purchaseDate) {
        this.userId = userId;
        this.courseId = courseId;
        this.purchaseDate = purchaseDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
