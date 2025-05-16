package com.codeelevate.stackoverflow_spring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


import java.util.ArrayList;
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

    @Transient
    @JsonProperty("createdByUsername")
    private String createdByUsername;

    // getter + setter pentru username (folosit doar la input JSON)

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }



    public Tag() {
        this.postsTags = new ArrayList<>();
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
        if(tagName==null){
            return;
        }
        this.tagName = tagName;
    }

    public void setTagDescription(String tagDescription) {
        if(tagDescription==null){
            return;
        }
        this.tagDescription = tagDescription;
    }

    public void setCreatedByUser(User createdByUser) {
        if(createdByUser==null){
            return;
        }
        this.createdByUser = createdByUser;
    }

    public void setPostTags(List<PostTag> postsTags) {
        this.postsTags = postsTags;
    }
}