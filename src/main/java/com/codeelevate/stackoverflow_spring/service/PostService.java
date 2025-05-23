package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    IVoteRepository voteRepository;
    @Autowired
    IPostTagRepository postTagRepository;
    @Autowired
    ICommentRepository commentRepository;

    public Post createPost(Post post) {

        Integer userId=post.getCreatedByUser().getId();
        User user=userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
        post.setCreatedByUser(user);

        String name = post.getPostType().getTypeName();
        Integer id = post.getPostType().getId();
        //PostType postType=postTypeRepository.findByPostTypeName(name);
        PostType postType=postTypeRepository.findById(id).orElse(null);
        post.setPostType(postType);
        assert postType != null;


        //if("question".equals(postType.getTypeName()) && post.getParentQuestion()==null){

        if(postType.getId()==1 && post.getParentQuestion()==null){
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
        //else if("answer".equals(postType.getTypeName()) && post.getParentQuestion()!=null){
        else  if(postType.getId()==2 && post.getParentQuestion()!=null){
            post.setStatusQ(null);
            post.setPostTitleQ(null);
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



    public Post getPostByID(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }

    public void deletePost(Integer idPost) {
        Post post = getPostByID(idPost);
        post.getCreatedByUser().getPosts().remove(post);
        for(Comment comment:post.getComments()){
            commentRepository.delete(comment);
        }
        for(Vote v:post.getVotes()){
            voteRepository.delete(v);
        }
        for(PostTag p:post.getPostTags()){
            postTagRepository.delete(p);
        }

        post.getPostType().getPosts().remove(post);

        if(post.getPostType().getTypeName().equals("answer")){
            post.getParentQuestion().getAnswers().remove(post);
        }
        else if(post.getPostType().getTypeName().equals("question")){
            for(Post x:post.getAnswers()){
                for(Comment comment:x.getComments()){
                    commentRepository.delete(comment);
                }
                for(Vote v:x.getVotes()){
                    voteRepository.delete(v);
                }
                for(PostTag p:x.getPostTags()){
                    postTagRepository.delete(p);
                }
            }
            //postRepository.deleteAll(post.getAnswers());
            for(Post p:post.getAnswers()){
                postRepository.delete(p);
            }
        }

        postRepository.delete(post);
    }

    public Post updatePost(Integer id,Post postDetails){
        return postRepository.findById(id).map(post -> {
            if (Objects.equals(post.getPostType().getTypeName(), "question") && post.getParentQuestion() == null) {
                post.setPostContent(postDetails.getPostContent());
                post.setPostTitleQ(postDetails.getPostTitleQ());
                post.setImg(postDetails.getImg());
            }
            else if (Objects.equals(post.getPostType().getTypeName(), "answer") && post.getParentQuestion() != null) {
                post.setPostContent(postDetails.getPostContent());
                post.setImg(postDetails.getImg());
            }
            else {
                throw new RuntimeException("Post not found");
            }
            return postRepository.save(post);
        }).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public List<Post> getAllPosts() {
        List<Post> x = (List<Post>) postRepository.findAll();
        x.sort(Comparator.comparing(Post::getPostedDate));
        return x;
    }

    public List<Post> getAllQuestions() {
        List<Post> x = (List<Post>) postRepository.findAll();
        List<Post> y = new ArrayList<>();
        for(Post p:x){
            if(p.getPostType().getTypeName().equals("question")){
                y.add(p);
            }
        }
        return y;
    }

    public List<Post> getAllAnswersForQuestion(Integer questionId) {
        List<Post> x = (List<Post>) postRepository.findAll();
        List<Post> y = new ArrayList<>();
        for(Post p:x){
            if(p.getPostType().getTypeName().equals("answer") && p.getParentQuestion().getId().equals(questionId)){
                y.add(p);
            }
        }
        return y;
    }

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

    public Optional<Post> getPostById(int id){
        return postRepository.findById(id);
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




}
