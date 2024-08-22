package com.dev.zine.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Event")
@Data
@NoArgsConstructor

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="id" , nullable = false)
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "venue")
    private String venue;

    @Column(name ="startDate")
    private Date startDateTime;

    @Column(name = "endDate")
    private Date endDateTime;

    @ManyToOne
    @JoinColumn(name="recruitment")
    private Recruitment recruitment;
}
