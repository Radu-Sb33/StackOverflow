package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.dto.VoteResponseDTO;
import com.codeelevate.stackoverflow_spring.entity.Comment;
import com.codeelevate.stackoverflow_spring.entity.Vote;
import com.codeelevate.stackoverflow_spring.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;


    @PostMapping("/createVote")
    @ResponseBody
    public Vote createVote(@RequestBody Vote vote) {
        return voteService.createVote(vote);
    }

    @DeleteMapping("/deleteVote/{id}")
    @ResponseBody
    public void deleteVote(@PathVariable Integer id) {
        voteService.deleteVote(id);
    }

    @GetMapping("/getVotesByPostId/{id}")
    @ResponseBody
    public List<VoteResponseDTO> getVotesByPostId(@PathVariable Integer id) {
        return voteService.getVotesForPost(id);
    }

    @GetMapping("/getScore/{id}")
    @ResponseBody
    public Integer getScore(@PathVariable Integer id) {
        return voteService.getScoreForQuestion(id);
    }



    @GetMapping("/getVoteByUserAndPost/{userId}/{postId}")
    @ResponseBody
    public VoteResponseDTO getVoteByUserAndPost(
            @PathVariable Integer userId,
            @PathVariable Integer postId) {
            return voteService.getVoteByUserAndPost(userId, postId);
    }
}
