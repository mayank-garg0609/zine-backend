package com.dev.zine.model;

import java.util.*;

import jakarta.persistence.*; // for Spring Boot 3
import lombok.Data;

@Entity
@Table(name = "Users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private String type;

    @Column(name = "pushToken")
    private String pushToken;

    @Column(name = "registered")
    private boolean registered;

    @Column(name = "dp")
    private int dp;

    @ManyToMany(mappedBy = "members")
    private Set<Rooms> userRooms = new HashSet<>();

}
