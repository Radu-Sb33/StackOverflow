package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "comment")
public class Comment {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @Getter
    @Setter
    @Column(name="comment_content")
    private String commentContent;

    @Getter
    @Setter
    @Column(name="posted_date")
    private Timestamp postedDate;

    public Comment(Integer id, Post post, User createdByUser, String commentContent, Timestamp postedDate) {
        this.id = id;
        this.post = post;
        this.createdByUser = createdByUser;
        this.commentContent = commentContent;
        this.postedDate = postedDate;
    }

    public Comment() {

    }
}

