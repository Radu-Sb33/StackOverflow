package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "vote")
public class Vote {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "vote_type_id", nullable = false)
    private VoteType voteType;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Getter
    @Setter
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
}

