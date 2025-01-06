package com.dev.zine.model.chat;

import java.sql.Timestamp;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.dev.zine.model.Media;
import com.dev.zine.model.Rooms;
import com.dev.zine.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_items")
@Data
@NoArgsConstructor
public class ChatItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "text_message")
    private TextMessage textMessage;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file")
    private Media file;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poll")
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "sent_from_user_id", nullable = false)
    private User sentFrom;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Rooms roomId;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "reply_to_message_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ChatItem replyTo;
}
