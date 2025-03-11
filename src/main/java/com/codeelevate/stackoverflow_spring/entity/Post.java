package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    //@Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Getter
//    @Setter
    @OneToMany(mappedBy="post")
    private List<Vote> votes;

//    @Getter
//    @Setter
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

//    @Getter
//    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_question_id")
    private Post parentQuestion;

//    @Getter
//    @Setter
    @OneToMany(mappedBy = "parentQuestion")
    private List<Post> answers;

//    @Getter
//    @Setter
    @ManyToOne
    @JoinColumn(name = "post_type_id", nullable = false)
    private PostType postType;

//    @Getter
//    @Setter
    @Column(name="post_title_q")
    private String postTitleQ;

//    @Getter
//    @Setter
    @Column(name="post_content")
    private String postContent;

//    @Getter
    @Column(name="posted_date")
    private Timestamp postedDate;

//    @Getter
//    @Setter
    @Column(name="img")
    private String img;

//    @Getter
//    @Setter
    @Column(name="status_q")
    private String statusQ;

//    @Getter
//    @Setter
    @OneToOne
    @JoinColumn(name = "accepted_answer_id", unique = true)
    private Post acceptedAnswer;

//    @Getter
//    @Setter
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

//    @Getter
//    @Setter
    @OneToMany(mappedBy ="post")
    private List<PostTag> postTags;

    public Post(Integer id, List<Vote> votes, User createdByUser, Post parentQuestion, List<Post> answers, PostType postType, String postTitleQ, String postContent, Timestamp postedDate, String img, String statusQ, Post acceptedAnswer, List<Comment> comments, List<PostTag> postTags) {
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
        this.postTags = postTags;
    }

    public Post() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Post getParentQuestion() {
        return parentQuestion;
    }

    public void setParentQuestion(Post parentQuestion) {
        this.parentQuestion = parentQuestion;
    }

    public List<Post> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Post> answers) {
        this.answers = answers;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getPostTitleQ() {
        return postTitleQ;
    }

    public void setPostTitleQ(String postTitleQ) {
        this.postTitleQ = postTitleQ;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Timestamp getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatusQ() {
        return statusQ;
    }

    public void setStatusQ(String statusQ) {
        this.statusQ = statusQ;
    }

    public Post getAcceptedAnswer() {
        return acceptedAnswer;
    }

    public void setAcceptedAnswer(Post acceptedAnswer) {
        this.acceptedAnswer = acceptedAnswer;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }
}
