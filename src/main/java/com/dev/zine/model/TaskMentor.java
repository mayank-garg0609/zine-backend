package com.dev.zine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "task_mentors", 
    uniqueConstraints = 
        {@UniqueConstraint(columnNames ={"task_id", "mentor"})})
@Data
@NoArgsConstructor
public class TaskMentor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task taskId;

    @ManyToOne
    @JoinColumn(name = "mentor")
    private User mentor;
}
