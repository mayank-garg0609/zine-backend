package com.dev.zine.model;

import java.sql.Timestamp;
import java.util.*;

import com.dev.zine.model.chat.ChatItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
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

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "dpUrl")
    private String dpUrl;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @JsonIgnore
    @OneToMany (mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<RoomMembers> roomMembers;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy="roomId")
    private TaskInstance taskInstance;

    @JsonIgnore
    @OneToMany(mappedBy = "roomId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "roomId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatItem> chatItems = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLastSeen> userLastSeens = new ArrayList<>();
}
