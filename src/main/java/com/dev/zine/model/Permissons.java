package com.dev.zine.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
public class Permissons {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="permission_id" , nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "action_id", nullable = false)
    private Actions action;

    @Column(name = "resource_id", nullable = true)
    private Long resource;
}
