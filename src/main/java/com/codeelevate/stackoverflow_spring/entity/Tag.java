package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tag")
public class Tag {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @Column(name="tag_name")
    private String tagName;

    @Getter
    @Setter
    @Column(name="tag_decsription")
    private String tagDescription;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;

    public Tag(Integer id, String tagName, String tagDescription, User createdByUser, List<Post> posts) {
        this.id = id;
        this.tagName = tagName;
        this.tagDescription = tagDescription;
        this.createdByUser = createdByUser;
        this.posts = posts;
    }

    public Tag() {

    }
}