package com.dev.zine.model;

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
@Table(name = "blog_components")
@Data

@NoArgsConstructor
public class BlogComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "component_type", nullable = false)
    private String componentType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "relative_order", nullable = false)
    private Integer relativeOrder;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;
}
