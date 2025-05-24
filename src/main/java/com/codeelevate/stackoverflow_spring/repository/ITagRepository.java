package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagRepository extends CrudRepository<Tag, Integer> {
   public Tag findByTagName(String name);
   @Query("SELECT t.tagName FROM Tag t WHERE t.id = :id")
   String findTagNameById(@Param("id") Integer id);
}
