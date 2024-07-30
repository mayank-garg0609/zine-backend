package com.dev.zine.model.TaskMentor;

import com.dev.zine.model.Task;
import com.dev.zine.model.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "task_mentors")
@Data
@IdClass(TaskMentorPKID.class)

public class TaskMentors {

//    //remove
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @Id
    @ManyToOne
    @JoinColumn(name ="task_id")
    private Task task_id;

    @Id
    @ManyToOne
    @JoinColumn(name="mentor")
    private User mentor;
}
