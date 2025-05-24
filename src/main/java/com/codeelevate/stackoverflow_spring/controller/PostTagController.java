package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.service.PostTagService;
import com.codeelevate.stackoverflow_spring.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/postTag")
public class PostTagController {
    @Autowired
    private PostTagService postTagService;

    @PostMapping("/createPostTag")
    @ResponseBody
    public PostTag createTag(@RequestBody PostTag postTag) {
        return postTagService.createPostTag(postTag);
    }

    @GetMapping("/getAllPostTags")
    @ResponseBody
    public List<PostTag> getAllPostTags() {
        return postTagService.getAllPostTags();
    }

    @GetMapping("/getByPost/{postId}")
    @ResponseBody
    public List<PostTag> getPostTagsByPostId(@PathVariable Integer postId) {
        return postTagService.getPostTagsByPostId(postId);
    }


}
