package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

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

    @Column(name = "description")
    private String description;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "ps_link")
    private String psLink;

    @Column(name = "submission_link")
    private String submissionLink;

    // @Column(name = "room_name")
    // private String roomName;

    // @Column(name = "create_room" , columnDefinition = "boolean default false")
    // private boolean createRoom;

    @Column(name = "type")
    private String type;

    @Column(name = "recruitment")
    private String recruitment;

    @Column(name = "visible")
    private boolean visible;

    // @OneToOne
    // @JoinColumn(name="roomId")
    // private Rooms roomId;
}
