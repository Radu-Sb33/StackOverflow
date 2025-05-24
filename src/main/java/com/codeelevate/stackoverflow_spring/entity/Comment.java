package com.codeelevate.stackoverflow_spring.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "comment")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Comment.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @Column(name="comment_content")
    private String commentContent;

    @Column(name="posted_date")
    private Timestamp postedDate;

    public Comment() {
        this.postedDate = new Timestamp(System.currentTimeMillis());
    }


    public Integer getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public void setCommentContent(String commentContent) {
        if(commentContent==null){return;}
        this.commentContent = commentContent;
    }

    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }
}

