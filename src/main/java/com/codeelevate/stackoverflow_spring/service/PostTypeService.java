package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.PostType;
import com.codeelevate.stackoverflow_spring.repository.IPostTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostTypeService {
    @Autowired
    IPostTypeRepository postTypeRepository;

    public PostType getPostTypeByName(String name) {
        return postTypeRepository.findByPostTypeName(name);
    }
}
