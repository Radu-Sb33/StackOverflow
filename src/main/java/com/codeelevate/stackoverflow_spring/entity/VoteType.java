package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vote_type")
public class VoteType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="vote_type")
    private String voteType;

    @OneToMany(mappedBy = "voteType")
    private List<Vote> votes;

    public VoteType() {
        this.votes = new ArrayList<>();
    }


    public Integer getId() {
        return id;
    }

    public String getVoteType() {
        return voteType;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}

