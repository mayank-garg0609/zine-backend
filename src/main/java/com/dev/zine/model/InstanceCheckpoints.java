package com.dev.zine.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "instance_checkpoints")
@Data
@NoArgsConstructor
public class InstanceCheckpoints {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "remark", columnDefinition = "boolean default true")
    private boolean remark;

    @Column(name = "content")
    private String content;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "sent_from", nullable = false)
    private User sentFrom;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_instance", nullable=false)
    private TaskInstance taskInstance;
}
