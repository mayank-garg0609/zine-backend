package com.dev.zine.model.UserTaskComment;

import com.dev.zine.model.User;
import com.dev.zine.model.UserTask;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "Task-db")
@Data
@IdClass(UserTaskCommentPKID.class)

public class UserTaskComments {

//    //remove it
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @Id
    @ManyToOne
    @Column(name="user_task_id")
    private UserTask user_task_id;


    @Column(name = "message" )
    private String message;

    @Id
    @ManyToOne
    @JoinColumn(name ="sender_id")
    private User sender_id;

    @Column(name="date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date date_time;

}
