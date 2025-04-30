package com.example.notification_service.kafka.event;

public class CourseCreateEvent {

    private long id;
    private String title;
    private Double price;
    private String instructor;
    private String courseCreatedDate;

    public CourseCreateEvent() {
    }

    public CourseCreateEvent(long id, Double price, String title, String instructor, String courseCreatedDate) {
        this.id = id;
        this.price = price;
        this.title = title;
        this.instructor = instructor;
        this.courseCreatedDate = courseCreatedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getCourseCreatedDate() {
        return courseCreatedDate;
    }

    public void setCourseCreatedDate(String courseCreatedDate) {
        this.courseCreatedDate = courseCreatedDate;
    }
}
