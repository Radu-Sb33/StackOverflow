package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    ITagRepository tagRepository;

    public void createTag(Tag tag) {
        tagRepository.save(tag);
    }



}
