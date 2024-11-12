package com.dev.zine.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "Users")
@Data
@ToString(exclude = {"verificationTokens", "taskMentors", "userTaskAssigned", "taskInstanceComments", "instanceCheckpoints", "instanceLinks", "hackathonRegistration"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "type", columnDefinition = "varchar(10) default 'user'")

    private String type = "user";

    @Column(name = "pushToken")
    private String pushToken;

    @Column(name = "registered")
    private boolean registered;

    @Column(name = "isEmailVerified", columnDefinition = "boolean default false")
    private boolean isEmailVerified;

    @Column(name = "dp")
    private String dp;

    @Column(name = "image_path")
    private String imagePath;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc")
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskMentor> taskMentors = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTaskAssigned> userTaskAssigned = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskInstanceComments> taskInstanceComments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sentFrom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstanceCheckpoints> instanceCheckpoints = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sentFrom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstanceLinks> instanceLinks = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private HackathonRegistrations hackathonRegistration;
}
