package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.service.PostService;
import com.codeelevate.stackoverflow_spring.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @PostMapping("/createTag")
    @ResponseBody
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }
}
