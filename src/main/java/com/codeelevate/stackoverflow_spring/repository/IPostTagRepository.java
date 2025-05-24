package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPostTagRepository extends CrudRepository<PostTag, Integer> {
    public Post findByPostId(Integer postId);
    public Tag findByTagId(Integer tagId);
    @Query("SELECT pt FROM PostTag pt WHERE pt.post.id = :postId")
    List<PostTag> findAllByPostId(@Param("postId") Integer postId);

}
