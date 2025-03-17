package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.repository.IPostTypeRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteTypeService {
    @Autowired
    IVoteTypeRepository voteTypeRepository;
}
