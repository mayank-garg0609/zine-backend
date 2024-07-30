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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "messages")
@Data

@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", columnDefinition = "varchar(255) default 'text'")
    private String type;

    @Column(name = "content")
    private String content;

    @Column(name = "conentUrl")
    private String contentUrl;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "sent_from_user_id", nullable = false)
    private User sentFrom;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms roomId;

    @ManyToOne
    @JoinColumn(name = "reply_to_message_id")
    private Message replyTo;

}
