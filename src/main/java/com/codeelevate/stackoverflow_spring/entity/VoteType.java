package com.codeelevate.stackoverflow_spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "vote_type")
public class VoteType {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @Column(name="vote_type")
    private String voteType;

    @Getter
    @Setter
    @OneToMany(mappedBy = "voteType")
    private List<Vote> votes;

    public VoteType(Integer id, String voteType, List<Vote> votes) {
        this.id = id;
        this.voteType = voteType;
        this.votes = votes;
    }

    public VoteType() {

    }
}

