package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "media")
@Data
@NoArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" , nullable = false)
    private Long id;

    @Column(name = "url")
    private String url;
    
    @Column(name = "public_id")
    private String publicId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
}
