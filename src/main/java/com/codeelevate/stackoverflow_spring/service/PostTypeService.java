package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.PostType;
import com.codeelevate.stackoverflow_spring.repository.IPostTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostTypeService {
    @Autowired
    IPostTypeRepository postTypeRepository;

    public PostType getPostTypeByName(String name) {
        return postTypeRepository.findByPostTypeName(name);
    }
    public Optional<PostType> getPostTypeByID(Integer id) {
        return postTypeRepository.findById(id);
    }

}
