package com.codeelevate.stackoverflow_spring.entity;
import serializer.PostIdListSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="user")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = User.class)
//@JsonIgnoreProperties({"email", "password", "about", "reputation", "img", "posts", "comments", "tags", "votes", "banned", "moderator"})
@JsonIgnoreProperties(value = {"votes", "posts", "comments", "tags"})
public class User {

   // @Getter
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   // @Setter
   // @Getter
    @Column(name="username")
    private String username;

   // @Setter
   // @Getter
    @Column(name="email")
    private String email;

  //  @Setter
  //  @Getter
    @Column(name="password")
    private String password;

//    @Setter
//    @Getter
    @Column(name="about")
    private String about;

    @JsonProperty("is_moderator")
    @Column(name="is_moderator")
    private Boolean isModerator;

//    @Setter
//    @Getter
    @Column(name="reputation")
    private Double reputation;

    @JsonProperty("is_banned")
    @Column(name="is_banned")
    private Boolean isBanned;

//    @Setter
//    @Getter
    @Column(name="img")
    private String img;

//    @Setter
//    @Getter
    @Column(name="creation_date")
    private Timestamp creationDate;


//    @Setter
//    @Getter
    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)
    @JsonSerialize(using = PostIdListSerializer.class)
    private List<Post> posts=new ArrayList<>();

//    @Setter
//    @Getter
    @OneToMany(mappedBy = "createdByUser")
    private List<Comment> comments;

//    @Setter
//    @Getter
    @OneToMany(mappedBy = "createdByUser", fetch = FetchType.LAZY)
    private List<Tag> tags;

//    @Setter
//    @Getter
    @OneToMany(mappedBy = "votedByUser")
    @JsonIgnore
    private List<Vote> votes;

    public User() {
        this.posts = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.votes = new ArrayList<>();
        this.isModerator = false;
        this.isBanned = false;
        this.reputation = 0.0;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public Boolean getModerator() {
        return this.isModerator;
    }

    public Boolean getBanned() {
        return isBanned;
    }

    public void setModerator(Boolean moderator) {
        if(moderator==null){return;}
        this.isModerator = moderator;
    }

    public void setBanned(Boolean banned) {
        if(banned==null){return;}
        isBanned = banned;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        if(username==null){return;}
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email==null){return;}
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password==null){return;}
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        if(about==null){return;}
        this.about = about;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        if(img==null){
            return;
        }
        this.img = img;
    }

    public Double getReputation() {
        return reputation;
    }

    public void setReputation(Double reputation) {
        this.reputation = reputation;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }
//
//    public void setCreationDate(Timestamp creationDate) {
//        this.creationDate = creationDate;
//    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Integer> getPostIDs(List<Post> posts) {
        List<Integer> postIDs = new ArrayList<>();
        for (Post post : posts) {
            postIDs.add(post.getId());
        }
        return postIDs;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", about='" + about + '\'' +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", moderator= " + isModerator +
                ", reputation=" + reputation +
                ", img='" + img + '\'' +
                ", creationDate=" + creationDate +
                ", posts=" + getPostIDs(posts) +
                ", comments=" + comments +
                ", tags=" + tags +
                ", votes=" + votes +
                '}';
    }
}