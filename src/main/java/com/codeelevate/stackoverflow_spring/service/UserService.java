package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.entity.Vote;
import com.codeelevate.stackoverflow_spring.entity.VoteType;
import com.codeelevate.stackoverflow_spring.repository.IUserRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;

    @Autowired
    IVoteTypeRepository voteTypeRepository;


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
            //user.setModerator(userDetails.getModerator()); //se face din admin
            user.setReputation(userDetails.getReputation());
            //user.setBanned(userDetails.getBanned()); //se face din admin
            user.setImg(userDetails.getImg());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public List<User> findAllUsersByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void calculateReputation(User user) {
        user.setReputation(0.0);
        for (Post post : user.getPosts()) {
            if(post.getPostType().getTypeName().equals("question")){
                for(Vote vote : post.getVotes()){
                    if(vote.getVoteType().getId()==1){
                        user.setReputation(user.getReputation() + 2.5);
                    }
                    else{user.setReputation(user.getReputation() - 1.5);}
                }
            }
            else if(post.getPostType().getTypeName().equals("answer")){
                for(Vote vote : post.getVotes()){
                    if(vote.getVoteType().getId()==1){
                        user.setReputation(user.getReputation() + 5);
                    }
                    else{user.setReputation(user.getReputation() - 2.5);}
                }
            }
        }
        //userRepository.save(user);
        List<VoteType> voteTypes = (List<VoteType>) voteTypeRepository.findAll();
        for(VoteType voteType : voteTypes){
            if(voteType.getId()==2){
                for(Vote vote:voteType.getVotes()){
                    if(vote.getVotedByUser() != null && vote.getVotedByUser().getId().equals(user.getId())){
                        user.setReputation(user.getReputation() - 1.5);
                    }
                }
            }
        }
        userRepository.save(user);
    }

}
