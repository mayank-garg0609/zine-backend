package com.dev.zine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;

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
@Table(name = "blogs")
@Data

@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "blog_id", nullable = false)
    private Long blogID;

    @Column(name = "blog_name")
    private String blogName;

    @Column(name = "blog_description", length = 1000)
    private String blogDescription;

    @Column(name = "content")
    private String content;

    @Column(name = "dp_url")
    private String dpURL;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name= "created_at")
    private Timestamp createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Blog parentBlog;
}