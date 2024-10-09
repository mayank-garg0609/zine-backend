package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "actions")
@Data
@NoArgsConstructor
public class Actions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="action_id" , nullable = false)
    private Long id;

    @Column(name = "action_name", nullable = false)
    private String actionName;
}
