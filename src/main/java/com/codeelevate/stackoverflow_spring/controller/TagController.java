package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.dto.TagDTO;
import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.service.PostService;
import com.codeelevate.stackoverflow_spring.service.TagService;
import com.codeelevate.stackoverflow_spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

//    @PostMapping("/createTag")
//    @ResponseBody
//    public Tag createTag(@RequestBody Tag tag) {
//        return tagService.createTag(tag);
//    }
@PostMapping("/createTag")
@ResponseBody
public Tag createTag(@RequestBody Tag tag) {
    String username = tag.getCreatedByUsername();
    System.out.println("Username primit: " + username);
    if (username == null) {
        throw new RuntimeException("Username-ul nu poate fi null");
    }

    User user = userService.findUserByUsername(username);
    System.out.println("User"+user);
    if (user == null) {
        throw new RuntimeException("User cu username-ul " + username + " nu a fost găsit");
    }

    // setezi user-ul pe tag
    tag.setCreatedByUser(user);

    // poți optional să ștergi createdByUsername să nu salvezi dublu
    tag.setCreatedByUsername(null);

    Tag createdTag=tagService.createTag(tag);
    // salvezi tag-ul cu user-ul
    if(tag==null) {
        return null;
    }
    return createdTag;
}

    @GetMapping("/getAllTags")
    @ResponseBody
    public List<TagDTO> getTags(){
        return tagService.getAllTags();
    }

    @GetMapping("/getTagNameById/{id}")
    @ResponseBody
    public String getTagNameById(@PathVariable Integer id) {
        String tagName = tagService.getTagNameById(id);
        return tagName;
    }
}
