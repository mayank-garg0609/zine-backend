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
@Table(name = "instance_links")
@Data
@NoArgsConstructor
public class InstanceLinks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "link")
    private String link;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_instance", nullable=false)
    private TaskInstance taskInstance;
}
