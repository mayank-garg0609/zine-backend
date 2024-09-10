package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="task_id" , nullable = false)
    private Long id;

    @Column( name ="createdDate" )
    private Date createdDate;

    @Column(name = "title" ,nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "ps_link")
    private String psLink;

    @Column(name = "submission_link")
    private String submissionLink;

    @Column(name = "type")
    private String type;

    @Column(name = "recruitment")
    private String recruitment;

    @Column(name = "visible")
    private boolean visible;

    @JsonIgnore
    @OneToMany(mappedBy = "taskId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskInstance> taskInstances = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "taskId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskMentor> taskMentors = new ArrayList<>();
}
