package com.codeelevate.stackoverflow_spring.repository;

import com.codeelevate.stackoverflow_spring.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public List<User> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.reputation = :newReputation WHERE u.id = :userId")
    double updateUserReputation(@Param("userId") Integer userId, @Param("newReputation") Double newReputation);

}
