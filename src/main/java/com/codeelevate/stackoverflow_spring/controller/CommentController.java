package com.codeelevate.stackoverflow_spring.controller;

import com.codeelevate.stackoverflow_spring.entity.Comment;
import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.service.CommentService;
import com.codeelevate.stackoverflow_spring.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    @ResponseBody
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }

    @DeleteMapping("/deleteComment/{id}")
    @ResponseBody
    public void deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
    }

    @GetMapping("/getAllComments/{postId}")
    @ResponseBody
    public List<Comment> getAllCommentsByPost(Integer postId) {
        return commentService.getAllCommentsByPost(postId);
    }

    @PutMapping("/updateComment/{idComm}")
    @ResponseBody
    public Comment updateComment(@PathVariable Integer idComm, @RequestBody Comment comment) {
        return commentService.updateComment(idComm,comment);
    }

}
