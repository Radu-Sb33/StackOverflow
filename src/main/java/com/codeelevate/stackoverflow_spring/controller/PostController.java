package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.service.PostService;
import com.codeelevate.stackoverflow_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @GetMapping("/getAllPosts")
    @ResponseBody
    public List<Post> getPosts(){
        return postService.getAllPosts();
    }

    @PostMapping("/createPost")
    @ResponseBody
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

//    @GetMapping("/getPostById/{id}")
//    @ResponseBody
//    public Optional<Post> getPostById(@PathVariable Integer id) {
//        return postService.getPostById(id);
//    }

    @GetMapping("/getAllPostsByTitleAndContent/{input}")
    @ResponseBody
    public List<Post> getAllPostsByTitleAndContent(@PathVariable String input) {
        return postService.getAllPostsByTitleAndContent(input);
    }

    @GetMapping("/getAllPostsByUser/{username}")
    @ResponseBody
    public List<Post> getAllPostsByUser(@PathVariable String user) {
        return postService.getAllPostsByUser(user);
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
            return postService.getAllPostsByTitleAndContent(query);
        }
        return null;

    }
}
