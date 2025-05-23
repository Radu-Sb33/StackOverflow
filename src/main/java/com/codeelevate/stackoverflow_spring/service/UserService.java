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

    @Autowired
    EmailSendingService emailSending;

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
        else {
            return Optional.empty();
        }
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
        System.out.println(userDetails);
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            //user.setPassword(PasswordEncryptionUtil.hashPassword(userDetails.getPassword()));
            user.setAbout(userDetails.getAbout());
            System.out.println(userDetails.getModerator());
            user.setModerator(userDetails.getModerator()); //se face din admin
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
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Integer findUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId) // Extrage ID-ul din obiectul User
                .orElse(null);  // Returnează null dacă utilizatorul nu există
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

    public boolean authenticateUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false; // No user found with the given email
        }

        User user = optionalUser.get();
        return PasswordEncryptionUtil.verifyPassword(password, user.getPassword());
    }

    public boolean emailExists(String email) {
        System.out.println("email"+email);
        for (User user : getAllUsers()) {
            if (email != null && email.equalsIgnoreCase(safeTrim(user.getEmail()))) {
                return true;
            }
        }
        return false;
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }


    public boolean usernameExists(String username) {
        return !userRepository.findByUsername(username).isEmpty();
    }

    public void banUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBanned(true);
        userRepository.save(user);

        //emailSending.sendSimpleMail(emailDetails);
        emailSending.sendBanNotification(user.getEmail(), user.getUsername());

    }


    public void unbanUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBanned(false);
        emailSending.sendUnbanNotification(user.getEmail(), user.getUsername());
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    public Optional<String> findUsernameById(Integer userId) {
        return userRepository.findUsernameById(userId);
    }


}
