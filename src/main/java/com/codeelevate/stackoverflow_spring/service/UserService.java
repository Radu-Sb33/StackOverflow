package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id);
        }
        else
            return Optional.empty();
    }

    public User updateUser(Integer id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setAbout(userDetails.getAbout());
            user.setModerator(userDetails.getModerator());
            user.setReputation(userDetails.getReputation());
            user.setBanned(userDetails.getBanned());
            user.setImg(userDetails.getImg());
            user.setCreationDate(userDetails.getCreationDate());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public void findAllUsersByUsername(String username) {
        userRepository.findByUsername(username);
    }
}
