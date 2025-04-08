package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.PostType;
import com.codeelevate.stackoverflow_spring.service.PostService;
import com.codeelevate.stackoverflow_spring.service.PostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostTypeService postTypeService;


    @GetMapping("/getAllPosts")
    @ResponseBody
    public List<Post> getPosts(){
        return postService.getAllPosts();
    }

    @DeleteMapping("/deletePost/{id}")
    @ResponseBody
    public void deletePost( @PathVariable Integer id) {
        postService.deletePost(id);
    }

    @PostMapping("/createPost")
    @ResponseBody
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Post>> getAllQuestions() {
        List<Post> questions = postService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<Post>> getAllAnswers(@PathVariable Integer questionId) {
        List<Post> answers = postService.getAllAnswersForQuestion(questionId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/getType/{id}")
    @ResponseBody
    public Optional<PostType> getPostType(@PathVariable Integer id) {
        return postTypeService.getPostTypeByID(id);
    }

    @GetMapping("/getPostById/{id}")
    @ResponseBody
    public Optional<Post> getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

//    @GetMapping("/getAllPostsByTitleAndContent/{input}")
//    @ResponseBody
//    public List<Post> getAllPostsByTitleAndContent(@PathVariable String input) {
//        return postService.getAllPostsByTitleAndContent(input);
//    }

    @GetMapping("/getAllPostsByUser/{username}")
    @ResponseBody
    public List<Post> getAllPostsByUser(@PathVariable String username) {
        return postService.getAllPostsByUser(username);
    }

    @PutMapping("/updatePost/{id}")
    @ResponseBody
    public Post updatePost(@PathVariable Integer id, @RequestBody Post postDetails) {
        return postService.updatePost(id, postDetails);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Post> searchPosts(@RequestParam("query") String query) {
        String regex = "\\[(\\w+)]|user:\\s*(\\w+)|answers:\\s*(\\d+)|score:\\s*(\\d+)|is:(?:question|answer)|isaccepted:(?:yes|no)";
        Pattern pattern = Pattern.compile(regex);
        boolean regFound = false;

        for (String searchCase : query.split("\\s+")) {
            Matcher matcher = pattern.matcher(searchCase);
            if (matcher.find()) {
                regFound = true;

                // [Tag]
                if (searchCase.matches("\\[\\w+]")) {
                    System.out.println("1");
                    return postService.getAllPostsByTag(matcher.group(1));
                }

                // user: string
                else if (searchCase.matches("user:\\s*\\w+")) {
                    return postService.getAllPostsByUser(matcher.group(2));
                }

                // answers: nr
                else if (searchCase.matches("answers:\\s*\\d+")) {
                    System.out.println("3");
                }

                // score: nr
                else if (searchCase.matches("score:\\s*\\d+")) {
                    System.out.println("4");
                }

                // is:question or is:answer
                else if (searchCase.matches("is:(question|answer)")) {
                    return postService.getAllPostsByType(matcher.group(5));
                }

                // isaccepted:yes
                else if (searchCase.matches("isaccepted:(yes|no)")) {
                    return postService.getAllPostsByAcceptedAnswerType(matcher.group(6));
                }

            }
        }
        if (!regFound) {
            System.out.println("No regex found");
            return postService.getAllPostsByTitleAndContentAndTag(query);
        }
        return null;

    }
}
