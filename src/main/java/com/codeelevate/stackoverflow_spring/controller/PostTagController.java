package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.service.PostTagService;
import com.codeelevate.stackoverflow_spring.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/postTag")
public class PostTagController {
    @Autowired
    private PostTagService postTagService;

    @PostMapping("/createPostTag")
    @ResponseBody
    public PostTag createTag(@RequestBody PostTag postTag) {
        return postTagService.createPostTag(postTag);
    }
}
