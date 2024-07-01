package com.dev.zine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blog_categories")
@Data

@NoArgsConstructor
public class BlogCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id", nullable = false)
    private Integer categoryID;

    @Column(name = "category")
    private String category;

    @Column(name = "dp_url")
    private String dpURL;
}