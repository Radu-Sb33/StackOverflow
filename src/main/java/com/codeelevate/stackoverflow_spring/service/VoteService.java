package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.dto.VoteResponseDTO;
import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.IPostRepository;
import com.codeelevate.stackoverflow_spring.repository.IUserRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void deleteVote(Integer vID)
    {
        Vote v = voteRepository.findById(vID).orElseThrow(() -> new RuntimeException("Vote not found with id: " + vID));
        User u = userService.getUserById(v.getVotedByUser().getId()).orElseThrow(() -> new RuntimeException("User not found with id: " + v.getVotedByUser().getId()));
        u.getVotes().remove(v);
        Post post = v.getPost();
        post.getVotes().remove(v);
        VoteType voteType = v.getVoteType();
        voteType.getVotes().remove(v);
        voteRepository.delete(v);
    }

    public List<VoteResponseDTO> getVotesForPost(Integer postId) {
        // 1. Găsește toate entitățile Vote pentru postId specificat
        List<Vote> votes = voteRepository.findByPostId(postId);

        // 2. Transformă fiecare entitate Vote într-un VoteResponseDTO
        return votes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Integer getScoreForQuestion(Integer questionId) {
        int score=0;
        List<Vote> votes = voteRepository.findByPostId(questionId);
        for(Vote vote : votes){
            if(vote.getVoteType().getId()==1){
                score+=1;
            }
            else{
                score-=1;
            }
        }
        return score;
    }

    /**
     * Metodă helper pentru a converti o entitate Vote într-un VoteResponseDTO.
     *
     * @param vote Entitatea Vote de convertit.
     * @return VoteResponseDTO corespunzător.
     */
    private VoteResponseDTO convertToDTO(Vote vote) {
        if (vote == null) {
            return null; // Sau aruncă o excepție, în funcție de logica dorită
        }

        VoteType voteTypeEntity = vote.getVoteType();
        String category = null;
        Integer voteTypeId = null;

        if (voteTypeEntity != null) {
            voteTypeId = voteTypeEntity.getId();
            // Presupunând că entitatea VoteType are o metodă getVoteType() care returnează string-ul (ex: "upvote")
            // Acest nume de metodă `getVoteType()` pe obiectul `voteTypeEntity` este bazat pe structura JSON anterioară.
            // Adaptează dacă numele metodei/câmpului este diferit în clasa ta VoteType.
            category = voteTypeEntity.getVoteType();
        }

        // Ne asigurăm că post și votedByUser nu sunt null înainte de a accesa ID-urile,
        // deși coloanele sunt marcate ca nullable=false în entitatea Vote.
        Integer actualPostId = (vote.getPost() != null) ? vote.getPost().getId() : null;
        Integer votedByUserId = (vote.getVotedByUser() != null) ? vote.getVotedByUser().getId() : null;


        return new VoteResponseDTO(
                vote.getId(),
                voteTypeId,
                actualPostId, // Ar trebui să fie același cu postId din argumentul metodei getVotesForPost
                votedByUserId
        );
    }

    public VoteResponseDTO getVoteByUserAndPost(Integer userId, Integer postId) {
        List<Vote> votes = voteRepository.findByPostId(postId);
        for(Vote vote : votes){
            if(vote.getVotedByUser().getId().equals(userId)){
                return convertToDTO(vote);
            }
        }
        return null;
    }

}
