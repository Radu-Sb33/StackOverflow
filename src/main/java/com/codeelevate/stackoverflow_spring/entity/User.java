package com.codeelevate.stackoverflow_spring.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name ="user")
public class User {

    @Getter
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    @Column(name="username")
    private String username;

    @Setter
    @Getter
    @Column(name="email")
    private String email;

    @Setter
    @Getter
    @Column(name="password")
    private String password;

    @Setter
    @Getter
    @Column(name="about")
    private String about;

    @Column(name="is_moderator")
    private Boolean isModerator;

    @Setter
    @Getter
    @Column(name="reputation")
    private Double reputation;

    @Column(name="is_banned")
    private Boolean isBanned;

    @Setter
    @Getter
    @Column(name="img")
    private String img;

    @Setter
    @Getter
    @Column(name="creation_date")
    private Timestamp creationDate;


    @Setter
    @Getter
    @OneToMany(mappedBy = "createdByUser")
    private List<Post> posts;

    @Setter
    @Getter
    @OneToMany(mappedBy = "createdByUser")
    private List<Comment> comments;

    @Setter
    @Getter
    @OneToMany(mappedBy = "createdByUser")
    private List<Tag> tags;

    @Setter
    @Getter
    @OneToMany(mappedBy = "votedByUser")
    private List<Vote> votes;

    public User() {}

    public User(Integer id, String username, String email, String password, String about, Boolean isModerator, Double reputation, Boolean isBanned, String img, Timestamp creationDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.about = about;
        this.isModerator = isModerator;
        this.reputation = reputation;
        this.isBanned = isBanned;
        this.img = img;
        this.creationDate = creationDate;
    }

    public Boolean getModerator() {
        return isModerator;
    }

    public Boolean getBanned() {
        return isBanned;
    }

    public void setModerator(Boolean moderator) {
        isModerator = moderator;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

}