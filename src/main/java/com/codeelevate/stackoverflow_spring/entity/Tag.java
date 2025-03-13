package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name="tag_name")
    private String tagName;


    @Column(name="tag_description")
    private String tagDescription;


    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;


    @OneToMany(mappedBy = "tag")
    private List<PostTag> postsTags;


    public Tag(Integer id, String tagName, String tagDescription, User createdByUser, List<PostTag> postsTags) {
        this.id = id;
        this.tagName = tagName;
        this.tagDescription = tagDescription;
        this.createdByUser = createdByUser;
        this.postsTags = postsTags;
    }

    public Tag() {

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

    public User getCreatedByUser() {
        return createdByUser;
    }

    public List<PostTag> getPostTags() {
        return postsTags;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public void setPostsTags(List<PostTag> postsTags) {
        this.postsTags = postsTags;
    }
}