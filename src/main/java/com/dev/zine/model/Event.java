package com.dev.zine.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Event")
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" , nullable = false)
    private long id;

    @NotNull
    @Column(name = "description", length = 1024)
    private String description;

    @NotNull
    @Column(name = "type")
    private String type;

    @NotNull
    private String dpUrl;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "venue")
    private String venue;

    @NotNull
    @Column(name ="startDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startDateTime;

    @Column(name = "endDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endDateTime;

    @ManyToOne
    @JoinColumn(name="recruitment")
    private Recruitment recruitment;
}
