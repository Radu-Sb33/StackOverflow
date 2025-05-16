package com.codeelevate.stackoverflow_spring.dto;

public class TagDTO {
    private Integer id;
    private String tagName;
    private String tagDescription;
    private String createdByUsername;

    public TagDTO(Integer id, String tagName, String tagDescription, String createdByUsername) {
        this.id = id;
        this.tagName = tagName;
        this.tagDescription = tagDescription;
        this.createdByUsername = createdByUsername;
    }

    public Integer getId() {
        return id;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }
}
