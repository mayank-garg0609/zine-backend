package com.dev.zine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "task_instance")
@Data
public class TaskInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="task_instance_id" , nullable = false)
    private Long taskInstanceId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task taskId;
    
    @OneToOne
    @JoinColumn(name="room_id")
    private Rooms roomId;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "completion_percentage")
    private Integer completion_percentage;
}
