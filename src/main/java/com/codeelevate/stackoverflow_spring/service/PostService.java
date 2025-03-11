package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.IPostRepository;
import com.codeelevate.stackoverflow_spring.repository.IPostTypeRepository;
import com.codeelevate.stackoverflow_spring.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class PostService {
    @Autowired
    IPostRepository postRepository;
    ITagRepository tagRepository;
    IPostTypeRepository postTypeRepository;

    public Post createPost(Post post) {
        if(Objects.equals(post.getPostType().getTypeName(), "question") && post.getParentQuestion() == null){
        post.setPostedDate((Timestamp) new Date());
        post.setStatusQ("Received");
        post.setCreatedByUser(post.getCreatedByUser());
        post.setPostTitleQ(post.getPostTitleQ());
        post.setPostContent(post.getPostContent());
        post.setImg(post.getImg());
        post.setParentQuestion(null);
        post.setAcceptedAnswer(null);
        }
        else if(Objects.equals(post.getPostType().getTypeName(), "answer") && post.getParentQuestion() != null){
            post.setStatusQ(null);
            post.setPostTitleQ(null);
            post.setPostedDate((Timestamp) new Date());
            post.setCreatedByUser(post.getCreatedByUser());
            post.setPostContent(post.getPostContent());
            post.setParentQuestion(post.getParentQuestion());
            post.setImg(post.getImg());
            post.setAcceptedAnswer(null);
        }
        else {throw new IllegalArgumentException("Invalid post type"); }
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return (List<Post>) postRepository.findAll();
    } //la getAllPosts trb sa le luam de la cea mai noua la cea mai veche

    public List<Post> getAllPostsByUser(String username) {
        List<Post> allPosts = new ArrayList<>();
        List<Post> posts = (List<Post>) postRepository.findAll();
        for (Post p : posts) {
            if(p.getCreatedByUser().getUsername().equals(username)) {
                allPosts.add(p);
            }
        }
        return allPosts;
    }

    public List<Post> getAllPostsByTitleAndContent(String titleContent) {
        return (List<Post>) postRepository.findByTitleAndContent(titleContent);
    }

    public List<Post> getAllPostsByTag(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        List<PostTag>listPostTags = tag.getPostTags();
        List<Post> listPosts = new ArrayList<>();
        for (PostTag pt : listPostTags) {
            Post p = pt.getPost();
            listPosts.add(p);
        }
        return listPosts;
    }

    public List<Post> getAllPostsByType(String postTypeName) {
        List<Post> posts = (List<Post>) postRepository.findAll();
        List<Post> questions = new ArrayList<>();
        List<Post> answers = new ArrayList<>();
        for (Post p : posts) {
            if (p.getPostType().getTypeName().equals("question")) {
                questions.add(p);
            } else {
                if (p.getPostType().getTypeName().equals("answer")) {
                    answers.add(p);
                }
            }
        }
        if("question".equals(postTypeName)){
            return questions;
        }
        else if("answer".equals(postTypeName)){
            return answers;
        }
        return null;
    }

    public List<Post> getAllPostsByAcceptedAnswerType(String typeIsAccepted) {
        PostType postType = postTypeRepository.findByPostTypeName("question");
        List<Post> questions = postType.getPosts();
        if ("yes".equals(typeIsAccepted)) {
            List<Post> solvedQuestions = new ArrayList<>();
            for (Post p : questions) {
                if(p.getAcceptedAnswer() != null) {
                    solvedQuestions.add(p);
                }
            }
            return solvedQuestions;
        }
        else if ("no".equals(typeIsAccepted)) {
            List<Post> unsolvedQuestions = postType.getPosts();
            for (Post p : questions) {
                if (p.getAcceptedAnswer() == null) {
                    unsolvedQuestions.add(p);
                }
            }
            return unsolvedQuestions;
        }
        return null;
    }


//    public List<Post> getAllAcceptedQuestions() {
//        return (List<Post>) postRepository.findByAcceptedQuestions();
//    }

//    public Optional<Post> getPostById(Integer id) {
//        if (postRepository.findById(id).isPresent()) {
//            return postRepository.findById(id);
//        }
//        else
//            return Optional.empty();
//    }

    public Post updatePost(Integer id, Post postDetails) {
        return postRepository.findById(id).map(post -> {
            if (Objects.equals(post.getPostType().getTypeName(), "question") && post.getParentQuestion() == null) {
                post.setPostTitleQ(postDetails.getPostTitleQ());
                post.setPostContent(postDetails.getPostContent());
             //   post.setStatusQ(postDetails.getStatusQ());
             //   post.setVoteCount(postDetails.getVoteCount());
             //   post.setViewCount(postDetails.getViewCount());

            }
            else if (Objects.equals(post.getPostType().getTypeName(), "answer") && post.getParentQuestion() != null) {
                post.setPostContent(postDetails.getPostContent());

             //   post.setStatusA(postDetails.getStatusA());
             //   post.setVoteCount(postDetails.getVoteCount());
             //   post.setViewCount(postDetails.getViewCount());
            }
            else {
                throw new RuntimeException("Post not found");
            }
            return postRepository.save(post);
        }).orElseThrow(() -> new RuntimeException("Post not found"));
    }


}
