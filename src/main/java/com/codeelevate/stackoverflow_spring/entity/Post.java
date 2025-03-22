package com.codeelevate.stackoverflow_spring.entity;

import com.codeelevate.stackoverflow_spring.service.UserService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")//,
        //scope = Post.class)
public class Post {

    //@Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Getter
//    @Setter
    //@JsonManagedReference
    @OneToMany(mappedBy="post")
    private List<Vote> votes;

//    @Getter
//    @Setter
    @ManyToOne()
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
    //@OneToOne
    @Column(name = "accepted_answer_id", unique = true)
    private Integer acceptedAnswer;

//    @Getter
//    @Setter
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

//    @Getter
//    @Setter
    @OneToMany(mappedBy ="post")
    private List<PostTag> postTags;


    public Post() {
        this.votes = new ArrayList<>();
        this.answers = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.postTags = new ArrayList<>();
        this.postedDate =new Timestamp(System.currentTimeMillis());

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
        if(postTitleQ==null){return;}
        this.postTitleQ = postTitleQ;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        if(postContent==null){return;}
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
        if(img==null){return;}
        this.img = img;
    }

    public String getStatusQ() {
        return statusQ;
    }

    public void setStatusQ(String statusQ) {
        if(statusQ==null){return;}
        this.statusQ = statusQ;
    }

    public Integer getAcceptedAnswer() {
        return acceptedAnswer;
    }

    public void setAcceptedAnswer(Integer acceptedAnswer) {
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


    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", votes=" + votes +
                ", createdByUser=" + createdByUser +
                ", parentQuestion=" + parentQuestion +
                ", answers=" + answers +
                ", postType=" + postType +
                ", postTitleQ='" + postTitleQ + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postedDate=" + postedDate +
                ", img='" + img + '\'' +
                ", statusQ='" + statusQ + '\'' +
                ", acceptedAnswer=" + acceptedAnswer +
                ", comments=" + comments +
                ", postTags=" + postTags +
                '}';
    }
}
