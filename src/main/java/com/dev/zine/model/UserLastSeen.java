package com.dev.zine.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_last_seen",uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "room_id"})})
@NoArgsConstructor
@Data
public class UserLastSeen {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms room;

    private Timestamp lastSeen;

    public UserLastSeen(User user, Rooms room) {
        this.user = user;
        this.room = room;
    }
}

