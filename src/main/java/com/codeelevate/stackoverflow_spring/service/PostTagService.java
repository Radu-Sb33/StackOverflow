package com.codeelevate.stackoverflow_spring.service;
import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.repository.IPostRepository;
import com.codeelevate.stackoverflow_spring.repository.IPostTagRepository;
import com.codeelevate.stackoverflow_spring.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PostTagService {
    @Autowired
    IPostTagRepository postTagRepository;
    @Autowired
    IPostRepository postRepository;
    @Autowired
    ITagRepository tagRepository;


    public PostTag createPostTag(PostTag postTag) {

        Integer postId = postTag.getPost().getId();
        Post post=postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
        postTag.setPost(post);

        Integer tagId = postTag.getTag().getId();
        Tag tag=tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId));
        postTag.setTag(tag);

        if (post.getPostTags() == null) {
            post.setPostTags(new ArrayList<>());
        }
        post.getPostTags().add(postTag);

        if (tag.getPostTags() == null) {
            tag.setPostTags(new ArrayList<>());
        }
        tag.getPostTags().add(postTag);

        return postTagRepository.save(postTag);
    }
}
