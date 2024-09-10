package com.dev.zine.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_instance_comments")
@Data
@NoArgsConstructor
public class TaskInstanceComments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "task_instance_id")
    private TaskInstance taskInstance;

    @Column(name = "message", length = 1000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "timestamp")
    private Timestamp timestamp;
}
