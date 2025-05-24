//package com.codeelevate.stackoverflow_spring.service;
//import com.codeelevate.stackoverflow_spring.entity.Post;
//import com.codeelevate.stackoverflow_spring.entity.PostTag;
//import com.codeelevate.stackoverflow_spring.entity.Tag;
//import com.codeelevate.stackoverflow_spring.entity.User;
//import com.codeelevate.stackoverflow_spring.repository.IPostRepository;
//import com.codeelevate.stackoverflow_spring.repository.IPostTagRepository;
//import com.codeelevate.stackoverflow_spring.repository.ITagRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//
//@Service
//public class PostTagService {
//    @Autowired
//    IPostTagRepository postTagRepository;
//    @Autowired
//    IPostRepository postRepository;
//    @Autowired
//    ITagRepository tagRepository;
//
//    @Autowired
//    UserService userService;
//
//
//    @Transactional // Important for operations involving multiple entities or modifications
//    public PostTag createPostTag(PostTag postTag) {
//        Tag tagDetailsFromRequest = postTag.getTag();
//
//        if (tagDetailsFromRequest != null) {
//            Tag managedTag;
//
//            // Option 1: If tag might be new or needs creator updated based on username
//            // (Similar to your TagController logic)
//            if (tagDetailsFromRequest.getId() == null && tagDetailsFromRequest.getTagName() != null) {
//                // Potentially a new tag or one where we must ensure the creator is set.
//                // Check if a tag with this name already exists to avoid duplicates, if necessary.
//                Tag existingTagByName = tagRepository.findByTagName(tagDetailsFromRequest.getTagName());
//                if (existingTagByName != null) {
//                    managedTag = existingTagByName;
//                    // Optionally update description if it changed
//                    if (tagDetailsFromRequest.getTagDescription() != null) {
//                        managedTag.setTagDescription(tagDetailsFromRequest.getTagDescription());
//                    }
//                } else {
//                    managedTag = new Tag();
//                    managedTag.setTagName(tagDetailsFromRequest.getTagName());
//                    managedTag.setTagDescription(tagDetailsFromRequest.getTagDescription());
//                }
//
//                // Set/Update createdByUser based on createdByUsername from the request
//                if (tagDetailsFromRequest.getCreatedByUsername() != null) {
//                    User user = userService.findUserByUsername(tagDetailsFromRequest.getCreatedByUsername());
//                    if (user == null) {
//                        throw new RuntimeException("User with username '" + tagDetailsFromRequest.getCreatedByUsername() + "' not found for tag.");
//                    }
//                    managedTag.setCreatedByUser(user);
//                    // If your Tag entity has a createdByUsername field and you want to keep it clean
//                    // managedTag.setCreatedByUsername(tagDetailsFromRequest.getCreatedByUsername()); // or null it out after setting User
//                } else if (managedTag.getCreatedByUser() == null) {
//                    // If username is not provided and not already set (e.g. for a new tag without explicit user)
//                    throw new RuntimeException("Tag creator username is required but was not provided.");
//                }
//                tagRepository.save(managedTag); // Save the new or updated tag
//            } else if (tagDetailsFromRequest.getId() != null) {
//                // Tag has an ID, assume it's an existing tag. Fetch it.
//                managedTag = tagRepository.findById(tagDetailsFromRequest.getId())
//                        .orElseThrow(() -> new RuntimeException("Tag with ID " + tagDetailsFromRequest.getId() + " not found."));
//                // You might still want to update the createdByUser if username is passed and differs,
//                // or if description is passed and differs. This depends on your business logic.
//                // For instance, if createdByUsername is passed, ensure it aligns or update:
//                if (tagDetailsFromRequest.getCreatedByUsername() != null &&
//                        (managedTag.getCreatedByUser() == null ||
//                                !managedTag.getCreatedByUser().getUsername().equals(tagDetailsFromRequest.getCreatedByUsername()))){
//                    User user = userService.findUserByUsername(tagDetailsFromRequest.getCreatedByUsername());
//                    if (user == null) {
//                        throw new RuntimeException("User with username '" + tagDetailsFromRequest.getCreatedByUsername() + "' not found for tag.");
//                    }
//                    managedTag.setCreatedByUser(user);
//                    tagRepository.save(managedTag);
//                }
//
//            } else {
//                throw new RuntimeException("Tag details (ID or Name) are insufficient.");
//            }
//            postTag.setTag(managedTag); // Set the managed tag (either new or fetched)
//        } else {
//            throw new RuntimeException("Tag information is missing in PostTag request.");
//        }
//
//        // Ensure the Post entity within PostTag is also managed if necessary
//        // if (postTag.getPost() != null && postTag.getPost().getId() == null) {
//        //     // Handle saving the Post if it's new and cascaded, or fetch if existing
//        // }
//
//
//        return postTagRepository.save(postTag);
//    }
//}

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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostTagService {

    @Autowired
    private IPostTagRepository postTagRepository;

    @Autowired
    private IPostRepository postRepository;

    @Autowired
    private ITagRepository tagRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public PostTag createPostTag(PostTag postTag) {
        Tag tagFromRequest = postTag.getTag();

        if (tagFromRequest == null) {
            throw new RuntimeException("Tag information is missing in PostTag request.");
        }

        Tag managedTag;

        if (tagFromRequest.getId() == null && tagFromRequest.getTagName() != null) {
            // Cazul 1: Nou tag sau identificat după nume
            Tag existingTag = tagRepository.findByTagName(tagFromRequest.getTagName());

            if (existingTag != null) {
                managedTag = existingTag;
                // Actualizează descrierea dacă e diferită
                if (tagFromRequest.getTagDescription() != null &&
                        !tagFromRequest.getTagDescription().equals(existingTag.getTagDescription())) {
                    managedTag.setTagDescription(tagFromRequest.getTagDescription());
                }
            } else {
                // Creăm tag nou
                managedTag = new Tag();
                managedTag.setTagName(tagFromRequest.getTagName());
                managedTag.setTagDescription(tagFromRequest.getTagDescription());
            }

            // Setăm creatorul dacă a fost trimis în request
            if (tagFromRequest.getCreatedByUsername() != null) {
                User creator = userService.findUserByUsername(tagFromRequest.getCreatedByUsername());
                if (creator == null) {
                    throw new RuntimeException("User with username '" + tagFromRequest.getCreatedByUsername() + "' not found.");
                }
                managedTag.setCreatedByUser(creator);
            } else if (managedTag.getCreatedByUser() == null) {
                throw new RuntimeException("Tag creator username is required but was not provided.");
            }

            tagRepository.save(managedTag);

        } else if (tagFromRequest.getId() != null) {
            // Cazul 2: Tag existent cu ID
            managedTag = tagRepository.findById(tagFromRequest.getId())
                    .orElseThrow(() -> new RuntimeException("Tag with ID " + tagFromRequest.getId() + " not found."));

            // Dacă a venit un username nou, îl setăm
            if (tagFromRequest.getCreatedByUsername() != null &&
                    (managedTag.getCreatedByUser() == null ||
                            !managedTag.getCreatedByUser().getUsername().equals(tagFromRequest.getCreatedByUsername()))) {
                User creator = userService.findUserByUsername(tagFromRequest.getCreatedByUsername());
                if (creator == null) {
                    throw new RuntimeException("User with username '" + tagFromRequest.getCreatedByUsername() + "' not found.");
                }
                managedTag.setCreatedByUser(creator);
                tagRepository.save(managedTag);
            }

        } else {
            throw new RuntimeException("Tag details (ID or Name) are insufficient.");
        }

        postTag.setTag(managedTag);

        // Dacă vrei să verifici și dacă Post-ul există în DB, o poți face așa:
        Post post = postTag.getPost();
        if (post != null && post.getId() != null) {
            Post dbPost = postRepository.findById(post.getId())
                    .orElseThrow(() -> new RuntimeException("Post with ID " + post.getId() + " not found."));
            postTag.setPost(dbPost); // Asigură-te că Post-ul e unul "managed"
        }

        return postTagRepository.save(postTag);
    }

    public List<PostTag> getAllPostTags()
    {
        return (List<PostTag>) postTagRepository.findAll();
    }
    public List<PostTag> getPostTagsByPostId(Integer postId) {
        return postTagRepository.findAllByPostId(postId);
    }

}
