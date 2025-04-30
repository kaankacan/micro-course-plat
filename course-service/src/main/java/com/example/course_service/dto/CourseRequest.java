package com.example.course_service.dto;

public class CourseRequest {

    private String title;
    private String description;
    private double price;
    private String instructor;


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public CourseRequest() {
    }
}
