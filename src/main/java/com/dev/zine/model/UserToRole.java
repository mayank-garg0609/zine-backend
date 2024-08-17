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
@Table(name = "user_to_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "role"})})
@Data
@NoArgsConstructor
public class UserToRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_role_id", nullable = false)
    private Long userRoleId;

    @ManyToOne
    @JoinColumn(name="id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name="role")
    private Role role;
}
