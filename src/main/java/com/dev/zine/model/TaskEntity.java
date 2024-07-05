package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Task-db")
@Data

@NoArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" , nullable = false)
    private long id;

    @Column( name ="createdDate" )
    private Date createdDate;

    @Column(name = "title" ,nullable = false)
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "description")
    private String description;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "ps_link")
    private String ps_Link;

    @Column(name = "submission_link")
    private String submission_link;

    @Column(name = "room_name")
    private String room_name;

    @Column(name = "create_room" , columnDefinition = "boolean default false")
    private boolean create_room;

    @Column(name = "type")
    private String type;

    @Column(name = "recruitment")
    private String recruitment;

    @Column(name = "visible")
    private boolean visible;
}
