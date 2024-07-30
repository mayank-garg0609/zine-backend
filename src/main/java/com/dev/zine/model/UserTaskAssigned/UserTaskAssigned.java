package com.dev.zine.model.UserTaskAssigned;

import com.dev.zine.model.Task;
import com.dev.zine.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_task_assigned")
@Data
@IdClass(UserTaskAssigned.class)

public class UserTaskAssigned {

    //remove it
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="id")
//    private int id;

    @Id
    @ManyToOne
    @JoinColumn(name="Task_id")
    private Task task_id;

    @Id
    @ManyToOne
    @JoinColumn(name ="User_id")
    private User user_id;

    @Column(name = "status" ,columnDefinition = "varchar(10) default 'Not Sarted'")
    private String status;
}
