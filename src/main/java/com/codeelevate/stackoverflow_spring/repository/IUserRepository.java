package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User, Integer> {
    public User findByUsername(String username);

}
