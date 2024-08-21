package com.dev.zine.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruitment", uniqueConstraints = @UniqueConstraint(columnNames = {"stage"}))
@Data
@NoArgsConstructor
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" , nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "stage")
    private Long stage;

    @Column(name = "description")
    private String description;
}
