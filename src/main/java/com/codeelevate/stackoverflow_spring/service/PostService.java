package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.IPostRepository;
import com.codeelevate.stackoverflow_spring.repository.IPostTypeRepository;
import com.codeelevate.stackoverflow_spring.repository.ITagRepository;
import com.codeelevate.stackoverflow_spring.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {
    @Autowired
    IPostRepository postRepository;
    @Autowired
    ITagRepository tagRepository;
    @Autowired
    IPostTypeRepository postTypeRepository;
    @Autowired
    IUserRepository userRepository;

    public Post createPost(Post post) {

        Integer userId=post.getCreatedByUser().getId();
        User user=userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
        post.setCreatedByUser(user);

        String name = post.getPostType().getTypeName();
        PostType postType=postTypeRepository.findByPostTypeName(name);
        post.setPostType(postType);

        if("question".equals(postType.getTypeName()) && post.getParentQuestion()==null){
            post.setStatusQ("Received");
            if (user.getPosts() == null) {
                user.setPosts(new ArrayList<>());
            }
            user.getPosts().add(post);
            if (postType.getPosts() == null) {
                postType.setPosts(new ArrayList<>());
            }
            postType.getPosts().add(post);

        }
        else if("answer".equals(postType.getTypeName()) && post.getParentQuestion()!=null){
            post.setStatusQ(null);

            Integer postQuestionId=post.getParentQuestion().getId();
            Post parentQuestion=postRepository.findById(postQuestionId).orElseThrow(() -> new RuntimeException("Question not found with id: " + postQuestionId));
            post.setParentQuestion(parentQuestion);


            if (user.getPosts() == null) {
               user.setPosts(new ArrayList<>());
           }
            user.getPosts().add(post);
            if (postType.getPosts() == null) {
                postType.setPosts(new ArrayList<>());
            }
            postType.getPosts().add(post);

            if (parentQuestion.getAnswers() == null) {
                parentQuestion.setAnswers(new ArrayList<>());
            }
            parentQuestion.getAnswers().add(post);

       }
        else {throw new IllegalArgumentException("Invalid post type"); }
        //postTypeRepository.save(post.getPostType());//de rezolvat
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        List<Post> x = (List<Post>) postRepository.findAll();
        x.sort(Comparator.comparing(Post::getPostedDate).reversed());
        return x;
    }//la getAllPosts trb sa le luam de la cea mai noua la cea mai veche

    public List<Post> getAllPostsByUser(String username) {
        List<Post> allPosts = new ArrayList<>();
        List<Post> posts = (List<Post>) postRepository.findAll();
        for (Post p : posts) {
            if(p.getCreatedByUser().getUsername().equals(username)) {
                allPosts.add(p);
            }
        }
        allPosts.sort(Comparator.comparing(Post::getPostedDate).reversed());
        return allPosts;
    }

    public List<Post> getAllPostsByTitleAndContentAndTag(String post_title_q) {
        List<Post> x =  (List<Post>) postRepository.findByTitleContentOrTag(post_title_q);
        x.sort(Comparator.comparing(Post::getPostedDate).reversed());
        return x;
    }

    public List<Post> getAllPostsByTag(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        List<PostTag>listPostTags = tag.getPostTags();
        List<Post> listPosts = new ArrayList<>();
        for (PostTag pt : listPostTags) {
            Post p = pt.getPost();
            listPosts.add(p);
        }
        listPosts.sort(Comparator.comparing(Post::getPostedDate).reversed());
        return listPosts;
    }

    public List<Post> getAllPostsByType(String type_name) {
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
        if("question".equals(type_name)){
            questions.sort(Comparator.comparing(Post::getPostedDate).reversed());
            return questions;
        }
        else if("answer".equals(type_name)){
            answers.sort(Comparator.comparing(Post::getPostedDate).reversed());
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
            solvedQuestions.sort(Comparator.comparing(Post::getPostedDate).reversed());
            return solvedQuestions;
        }
        else if ("no".equals(typeIsAccepted)) {
            List<Post> unsolvedQuestions = postType.getPosts();
            for (Post p : questions) {
                if (p.getAcceptedAnswer() == null) {
                    unsolvedQuestions.add(p);
                }
            }
            unsolvedQuestions.sort(Comparator.comparing(Post::getPostedDate));
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
