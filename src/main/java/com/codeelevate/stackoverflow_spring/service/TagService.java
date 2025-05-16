package com.codeelevate.stackoverflow_spring.service;

import com.codeelevate.stackoverflow_spring.dto.TagDTO;
import com.codeelevate.stackoverflow_spring.entity.Post;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.codeelevate.stackoverflow_spring.entity.User;
import com.codeelevate.stackoverflow_spring.repository.ITagRepository;
import com.codeelevate.stackoverflow_spring.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TagService {
    @Autowired
    ITagRepository tagRepository;
    @Autowired
    IUserRepository userRepository;
    public Tag createTag(Tag tag) {

        Integer userId=tag.getCreatedByUser().getId();
        User user=userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
        tag.setCreatedByUser(user);

        if (user.getTags() == null) {
            user.setTags(new ArrayList<>());
        }
        user.getTags().add(tag);

        return tagRepository.save(tag);
    }

    public List<TagDTO> getAllTags() {
        List<Tag> tags = (List<Tag>) tagRepository.findAll();
        tags.sort(Comparator.comparing(Tag::getTagName));

        // Mapăm fiecare Tag -> TagDTO
        return tags.stream()
                .map(tag -> new TagDTO(
                        tag.getId(),
                        tag.getTagName(),
                        tag.getTagDescription(),
                        tag.getCreatedByUser().getUsername()
                ))
                .toList();
    }

}
