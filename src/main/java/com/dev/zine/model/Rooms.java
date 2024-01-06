package com.dev.zine.model;

import java.sql.Timestamp;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Rooms")
@Data
@NoArgsConstructor
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "dpUrl")
    private String dpUrl;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @JsonIgnore
    @ManyToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<RoomMembers> roomMembers;

    // @JsonIgnore
    // @ManyToMany(mappedBy = "roomId", cascade = CascadeType.REMOVE)
    // @OrderBy("id desc")
    // private List<Message> messages;

}
