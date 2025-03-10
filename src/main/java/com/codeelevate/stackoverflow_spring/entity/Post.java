package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @OneToMany(mappedBy="post")
    private List<Vote> votes;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_question_id")
    private Post parentQuestion;

    @Getter
    @Setter
    @OneToMany(mappedBy = "parentQuestion")
    private List<Post> answers;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "post_type_id", nullable = false)
    private PostType postType;

    @Getter
    @Setter
    @Column(name="post_title_q")
    private String postTitleQ;

    @Getter
    @Setter
    @Column(name="post_content")
    private String postContent;

    @Getter
    @Column(name="posted_date")
    private Timestamp postedDate;

    @Getter
    @Setter
    @Column(name="img")
    private String img;

    @Getter
    @Setter
    @Column(name="status_q")
    private String statusQ;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "accepted_answer_id", unique = true)
    private Post acceptedAnswer;

    @Getter
    @Setter
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @Getter
    @Setter
    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    public Post(Integer id, List<Vote> votes, User createdByUser, Post parentQuestion, List<Post> answers, PostType postType, String postTitleQ, String postContent, Timestamp postedDate, String img, String statusQ, Post acceptedAnswer, List<Comment> comments, List<Tag> tags) {
        this.id = id;
        this.votes = votes;
        this.createdByUser = createdByUser;
        this.parentQuestion = parentQuestion;
        this.answers = answers;
        this.postType = postType;
        this.postTitleQ = postTitleQ;
        this.postContent = postContent;
        this.postedDate = postedDate;
        this.img = img;
        this.statusQ = statusQ;
        this.acceptedAnswer = acceptedAnswer;
        this.comments = comments;
        this.tags = tags;
    }

    public Post() {

    }
}
