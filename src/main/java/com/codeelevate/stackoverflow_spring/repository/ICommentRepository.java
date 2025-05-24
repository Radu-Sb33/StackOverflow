package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends CrudRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    public List<Comment> getCommentsByPostId(int postId);
}
