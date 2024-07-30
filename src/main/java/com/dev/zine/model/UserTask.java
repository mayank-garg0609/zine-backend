package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserTask")
@Data
@NoArgsConstructor

public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" , nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn( name ="task_id" )
    private Task task_id;

    @Column(name = "task_status" , columnDefinition = "varchar(20) default 'Not Started'")
    private String task_status;

    @Column(name = "completion_percentage" , columnDefinition = "Integer default 0")
    private Integer completion_percentage;

    @ManyToOne
    @JoinColumn(name="room_id")
    private Rooms room_id;
}
