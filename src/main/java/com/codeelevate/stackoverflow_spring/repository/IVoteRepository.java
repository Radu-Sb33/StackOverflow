package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVoteRepository extends CrudRepository<Vote, Integer> {
    List<Vote> findByPostId(Integer postId);
}
