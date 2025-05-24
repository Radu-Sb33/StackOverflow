package com.codeelevate.stackoverflow_spring.dto; // Sau pachetul corespunzător

public class VoteResponseDTO {
    private Integer id;
    private Integer voteTypeId;

    private Integer postId;
    private Integer votedByUserId;
    // Poți adăuga și alte câmpuri simple dacă sunt necesare

    // Constructor, Getters, Setters
    public VoteResponseDTO(Integer id, Integer voteTypeId, Integer postId, Integer votedByUserId) {
        this.id = id;
        this.voteTypeId = voteTypeId;

        this.postId = postId;
        this.votedByUserId = votedByUserId;
    }

    // Getters și setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getVoteTypeId() { return voteTypeId; }
    public void setVoteTypeId(Integer voteTypeId) { this.voteTypeId = voteTypeId; }
    public Integer getPostId() { return postId; }
    public void setPostId(Integer postId) { this.postId = postId; }
    public Integer getVotedByUserId() { return votedByUserId; }
    public void setVotedByUserId(Integer votedByUserId) { this.votedByUserId = votedByUserId; }
}