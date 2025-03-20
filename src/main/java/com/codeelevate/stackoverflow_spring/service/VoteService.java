package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.IPostRepository;
import com.codeelevate.stackoverflow_spring.repository.IUserRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class VoteService {
    @Autowired
    IVoteRepository voteRepository;
    @Autowired
    IPostRepository postRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IVoteTypeRepository voteTypeRepository;

    @Autowired
    UserService userService;

    public Vote createVote(Vote vote) {

        Integer postId = vote.getPost().getId();
        Post post=postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        vote.setPost(post);

        Integer userId=vote.getVotedByUser().getId();
        User user=userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
        vote.setVotedByUser(user);

        if (user.getVotes() == null) {
            user.setVotes(new ArrayList<>());
        }
        user.getVotes().add(vote);

        if (post.getVotes() == null) {
            post.setVotes(new ArrayList<>());
        }
        post.getVotes().add(vote);


        Integer voteTypeId = vote.getVoteType().getId();
        VoteType voteType=voteTypeRepository.findById(voteTypeId).orElseThrow(() -> new RuntimeException("VoteType not found with id: " + voteTypeId));
        vote.setVoteType(voteType);
        if (voteType.getVotes() == null) {
            voteType.setVotes(new ArrayList<>());
        }
        voteType.getVotes().add(vote);

        User postOwner = post.getCreatedByUser();
        if(postOwner!=null){
            userService.calculateReputation(postOwner);}
        return voteRepository.save(vote);
    }

    public void deleteVote(Vote v)
    {
        voteRepository.delete(v);
    }
}
