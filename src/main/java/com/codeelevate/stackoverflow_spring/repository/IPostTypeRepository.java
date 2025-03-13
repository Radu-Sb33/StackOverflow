package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.PostType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPostTypeRepository extends CrudRepository<PostType, Integer> {
    @Query("SELECT p FROM PostType p WHERE p.typeName = :name")
    public PostType findByPostTypeName(@Param("name") String name);




}
