package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.entity.PostType;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostRepository extends CrudRepository<Post, Integer> {
    List<PostTag> findByPost(Post post);
    List<Post> findByTitleAndContent(String title);//trb implementat
    List<Post> findByTagContaining(Tag tag);
    List<Post> findByUser(String username); // regex (user:1234 => 1234)
    List<Post> findByAnswers(String answers); //regex pt a determina nr rasp(ex. "answers:0" => 0)
    List<Post> findByScore(String score); //regex pt a determina nr score(ex. "score:0" => 0)
    List<Post> findByType(PostType type);
    List<Post> findByAcceptedQuestions(Post post);//avem valoare in accepted_answer_id=> status=closed
    @Query("SELECT p FROM Post p " +
            "JOIN p.postTags pt " +
            "WHERE LOWER(p.postTitleQ) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.postContent) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(pt.tag.tagName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> findByTitleContentOrTag(String keyword);
}
