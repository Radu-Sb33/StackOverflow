package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "post_type")
public class PostType {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @OneToMany(mappedBy = "postType")
    private List<Post> posts;

    @Setter
    @Getter
    @Column(name="type_name")
    private String typeName;

    public PostType(Integer id, List<Post> posts, String typeName) {
        this.id = id;
        this.posts = posts;
        this.typeName = typeName;
    }

    public PostType() {

    }
}