package com.example.course_service.dto;

public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private String instructor;

    public CourseResponse() {
    }

    public CourseResponse(Long id, String title, String description, Double price, String instructor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.instructor = instructor;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
