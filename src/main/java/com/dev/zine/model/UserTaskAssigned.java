package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_task_assigned", 
            uniqueConstraints = @UniqueConstraint(columnNames = {"task_instance_id", "user_id"}))
@Data
@NoArgsConstructor
public class UserTaskAssigned {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="task_instance_id")
    private TaskInstance taskInstanceId;
    
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User userId;
}
