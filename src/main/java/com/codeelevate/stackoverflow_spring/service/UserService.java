package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.PasswordEncryptionUtil;
import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.*;
import com.codeelevate.stackoverflow_spring.repository.IVoteRepository;
import com.codeelevate.stackoverflow_spring.repository.IVoteTypeRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    IPostRepository postRepository;

    @Autowired
    IVoteRepository voteRepository;

    @Autowired
    ICommentRepository commRepository;

    @Autowired
    ITagRepository tagRepository;

    public User createUser(User user) {
        user.setPassword(PasswordEncryptionUtil.hashPassword(user.getPassword()));
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

    public User getById(Integer id) {
        for (User user : getAllUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public User updateUser(Integer id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            //user.setPassword(PasswordEncryptionUtil.hashPassword(userDetails.getPassword()));
            user.setAbout(userDetails.getAbout());
            //user.setModerator(userDetails.getModerator()); //se face din admin
//            user.setReputation(userDetails.getReputation());
            //user.setBanned(userDetails.getBanned()); //se face din admin
            user.setImg(userDetails.getImg());

            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(PasswordEncryptionUtil.hashPassword(userDetails.getPassword()));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    //@Transactional
    public void deleteUser(Integer id) {
       User deleted = this.getById(-1);
//       deleted.setId(-1);
        for(Tag tag: this.getById(id).getTags()){
            tag.setCreatedByUser(deleted);
            tagRepository.save(tag);
        }
        for(Post p: this.getById(id).getPosts()){
            p.setCreatedByUser(deleted);
            postRepository.save(p);
        }
        for(Comment comment: this.getById(id).getComments()){
            comment.setCreatedByUser(deleted);
            commRepository.save(comment);
        }
        for(Vote v: this.getById(id).getVotes()){
            v.setVotedByUser(deleted);
            voteRepository.save(v);
        }
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
                    else{
                        vote.getVotedByUser().setReputation(vote.getVotedByUser().getReputation() - 1.5);
                        user.setReputation(user.getReputation() - 2.5);}
                }
            }
        }

//        List<Vote> voturi = (List<Vote>) voteRepository.findAll();
//        for (Vote vote : voturi) {
//            if(vote.getVoteType().getId()==2){
//                if(vote.getPost().getPostType().getTypeName().equals("answer")){
//                    vote.getVotedByUser().setReputation(vote.getVotedByUser().getReputation() - 1.5);
//                }
//            }
//        }
    }

    public boolean authenticateUser(String username, String password) {
        User user = (User) userRepository.findByUsername(username);
        if (user == null) return false;

        return PasswordEncryptionUtil.verifyPassword(password, user.getPassword());
    }

}
