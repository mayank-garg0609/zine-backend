package com.dev.zine.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruitment")
@Data
@NoArgsConstructor
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" , nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "stage", unique = true)
    private Long stage;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();
}
