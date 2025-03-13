package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.PostTag;
import org.springframework.data.repository.CrudRepository;

public interface IPostTagRepository extends CrudRepository<PostTag, Integer> {

}
