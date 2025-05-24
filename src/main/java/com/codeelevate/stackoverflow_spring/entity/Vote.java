package com.codeelevate.stackoverflow_spring.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
@Table(name = "vote")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Vote.class)
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "vote_type_id", nullable = false)
    private VoteType voteType;


    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference
    @JsonIgnoreProperties({"votes", "comments", "tags"})
    private Post post;


    @ManyToOne()
    @JoinColumn(name = "voted_by_user_id", nullable = false)
    private User votedByUser;

    public Vote(Integer id, VoteType voteType, Post post, User votedByUser) {
        this.id = id;
        this.voteType = voteType;
        this.post = post;
        this.votedByUser = votedByUser;
    }

    public Vote() {

    }

    public Integer getId() {
        return id;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public Post getPost() {
        return post;
    }

    public User getVotedByUser() {
        return votedByUser;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVoteType(VoteType voteType) {
        if(voteType==null){return;}
        this.voteType = voteType;
    }

    public void setPost(Post post) {
        if(post==null){return;}
        this.post = post;
    }

    public void setVotedByUser(User votedByUser) {
        if(votedByUser==null){return;}
        this.votedByUser = votedByUser;
    }
}

