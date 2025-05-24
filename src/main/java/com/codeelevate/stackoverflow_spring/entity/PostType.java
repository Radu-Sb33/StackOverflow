package com.codeelevate.stackoverflow_spring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "post_type")
public class PostType {
//    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Getter
//    @Setter
    @OneToMany(mappedBy = "postType")
    @JsonBackReference
    private List<Post> posts;

//    @Setter
//    @Getter
    @Column(name="type_name")
    private String typeName;

    public PostType(Integer id, List<Post> posts, String typeName) {
        this.id = id;
        this.posts = posts;
        this.typeName = typeName;
    }

    public PostType() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}