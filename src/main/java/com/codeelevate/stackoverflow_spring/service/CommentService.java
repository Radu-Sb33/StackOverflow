package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.PasswordEncryptionUtil;
import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.ICommentRepository;
import com.codeelevate.stackoverflow_spring.repository.IPostRepository;
import com.codeelevate.stackoverflow_spring.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    ICommentRepository commentRepository;

    @Autowired
    IPostRepository postRepository;

    @Autowired
    IUserRepository userRepository;

    public Comment createComment(Comment comment) {

        Integer postId = comment.getPost().getId();
        Post post=postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        comment.setPost(post);

        Integer userId=comment.getCreatedByUser().getId();
        User user=userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
        comment.setCreatedByUser(user);

        if (user.getComments() == null) {
            user.setComments(new ArrayList<>());
        }
        user.getComments().add(comment);

        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }
        post.getComments().add(comment);

        return commentRepository.save(comment);
    }

    public Comment getCommByID(Integer commID) {
        return commentRepository.findById(commID).orElseThrow(() -> new RuntimeException("Comment not found with id: " + commID));
    }

    public void deleteComment(Integer id){
        Comment c = getCommByID(id);
        commentRepository.delete(c);
    }

    public List<Comment> getAllCommentsByPost(Integer postId){
        return commentRepository.getCommentsByPostId(postId);
    }

    public Comment updateComment(Integer id, Comment commDetails) {
        return commentRepository.findById(id).map(c -> {
            c.setCommentContent(commDetails.getCommentContent());
            return commentRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

}
