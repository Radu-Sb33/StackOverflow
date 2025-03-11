package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.PostType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostTypeRepository extends CrudRepository<PostType, Integer> {
    public PostType findByPostTypeName(String postTypeName);

//    @Query("SELECT p FROM Post p " +
//            "JOIN p.postTags pt " +
//            "WHERE LOWER(p.postTitleQ) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(p.postContent) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(pt.tag.tagName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    List<Post> findByTitleContentOrTag(@Param("keyword") String keyword);


}
