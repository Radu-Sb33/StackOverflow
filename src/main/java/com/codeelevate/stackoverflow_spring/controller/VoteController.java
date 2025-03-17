package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.Comment;
import com.codeelevate.stackoverflow_spring.entity.Vote;
import com.codeelevate.stackoverflow_spring.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;


    @PostMapping("/createVote")
    @ResponseBody
    public Vote createVote(@RequestBody Vote vote) {
        return voteService.createVote(vote);
    }
}
