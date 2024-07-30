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
    private Date start_date_time;

    @Column(name = "endDate")
    private Date end_date_time;

//    @OneToOne ???
//    @JoinColumn(name="recruitment")
//    private Recruitment id;
}
