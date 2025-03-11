package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vote")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "vote_type_id", nullable = false)
    private VoteType voteType;


    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    @ManyToOne
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
        this.voteType = voteType;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setVotedByUser(User votedByUser) {
        this.votedByUser = votedByUser;
    }
}

